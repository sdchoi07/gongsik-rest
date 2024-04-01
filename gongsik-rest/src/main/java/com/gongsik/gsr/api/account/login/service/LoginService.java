package com.gongsik.gsr.api.account.login.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.jwt.PrincipalDetails;
import com.gongsik.gsr.global.jwt.JwtAuthorizationFilter;
import com.gongsik.gsr.global.jwt.JwtProvider;
import com.gongsik.gsr.global.vo.ResultVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	private final JwtProvider jwtProvider;
	private final HttpServletResponse response;
	/* 회원 로그인 */
	public Map<String, Object> accountData(Map<String, String> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String usrId = map.get("usrId");
		Optional<AccountEntity> usrChk = accountRepository.findByUsrId(usrId);
		if (usrChk.isPresent()) {
			// DB 정보 가져오기
			String accountId = usrChk.get().getUsrId();
			String accountPwd = usrChk.get().getUsrPwd();
			// authenication 등록
			Authentication authentication = jwtProvider.getAuthentication(accountId, accountPwd);
			// access_token 생성 후 localstroge에 저장
			String accessToken = jwtProvider.createToken(usrId);
			// refresh_token 생성후 redis,DB에 저장
			jwtProvider.refreshToken(usrId);

			PrincipalDetails principalDetails = new PrincipalDetails(usrChk.get());

			// Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
			authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
					principalDetails.getAuthorities());
			// 강제로 시큐리티의 세션에 접근하여 authentication 객체를 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// 로그인시 해당 계정 로그인 시간 업데이트
			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formatterDate = date.format(formatter);
			String refesthToken = redisTemplate.opsForValue().get("refreshToken");
			usrChk.get().setUsrLogInDt(formatterDate);
			usrChk.get().setRefreshToken(refesthToken);
			accountRepository.save(usrChk.get());

//			result.put("accessToken", "Bearer " + accessToken);
			
			response.addHeader("Authorization", "Bearer " + accessToken);
			result.put("usrId", usrChk.get().getUsrId());
			result.put("logTp", usrChk.get().getLogTp());
			result.put("usrRole", usrChk.get().getUsrRole());
		} else {
			result.put("code", "fail");
			result.put("msg", "가입된 정보가 없습니다");
		}

		return result;
	}

//	/* 회원 로그인 */
//	public Map<String, Object> accountData(Authentication authentication) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 로그인시 해당 계정 로그인 시간 업데이트
//		LocalDateTime date = LocalDateTime.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		String formatterDate = date.format(formatter);
//		
//		String usrId = authentication.getPrincipal().toString();
//		Optional<AccountEntity> list = accountRepository.findByUsrId(usrId);
//		String accessToken = "";
//		String refreshToken = "";
//		if (list.isPresent()) {
//			// 토큰 발행
//			accessToken = jwtProvider.createToken(list.get().getUsrNm());
//			refreshToken = jwtProvider.refreshToken(list.get().getUsrNm());
//			list.get().setUsrLogInDt(formatterDate);
//			list.get().setRefreshToken(refreshToken);
//			accountRepository.save(list.get());
//		}
//
//		PrincipalDetails principalDetails = new PrincipalDetails(list.get());
//
//		// Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
//		 authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
//				principalDetails.getAuthorities());
//		// 강제로 시큐리티의 세션에 접근하여 authentication 객체를 저장
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//
//		map.put("accessToken", "Bearer "+accessToken);
//		map.put("usrId", list.get().getUsrId());
//		map.put("logTp", list.get().getLogTp());
//		map.put("usrRole", list.get().getUsrRole());
//		return map;
//	}

	public void logout(Map<String, String> map) {
		String usrId = map.get("usrId");
		String logTp = map.get("logTp");
		Date now = new Date(System.currentTimeMillis() + (600000 * 10));
		String refreshToken = map.get("refreshToken");

		Optional<AccountEntity> list = accountRepository.findByUsrIdAndLogTp(usrId, logTp);
		if (!list.isPresent()) {
			new LoginException();
		}
		// **로그아웃 구분하기 위해 redis에 저장**
		redisTemplate.delete("refreshToken");
	}

	public ResultVO SNSLogin(Map<String, String> map) {
		ResultVO resultVo = new ResultVO();

		String provider = map.get("provider");
		String providerId = map.get("providerId");
		String email = map.get("email");
		String role = map.get("role");
		String logTp = map.get("logTp");

		Optional<AccountEntity> accountEntity = accountRepository.findByUsrIdAndProviderIdAndLogTp(email, providerId,
				logTp);
		AccountEntity result = new AccountEntity();
		if (accountEntity.isEmpty()) {
			result.setUsrId(email);
			result.setProvider(provider);
			result.setProviderId(providerId);
			result.setUsrRole(role);
			result.setLogTp(logTp);
			accountRepository.save(result);
		} else {
			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formatterDate = date.format(formatter);
			result.setUsrLogInDt(formatterDate);
			accountRepository.save(result);
		}

		return resultVo;
	}

	public boolean tokenChk(Map<String, String> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String usrId = map.get("usrId");
		String logTp = map.get("logTp");

		// refresh 토큰 유효 체크
		String redisRefreshToken = jwtProvider.getStoredRefreshToken();
		// DB refresh 토큰
		Optional<AccountEntity> accountEntity = accountRepository.findByUsrIdAndLogTp(usrId, logTp);
		String dbRefreshToken = accountEntity.get().getRefreshToken();
		// 아직 유효 하다면 세션 유지
		if (dbRefreshToken.equals(redisRefreshToken)) {

			String usrNm = accountEntity.get().getUsrNm();
			String accessToken = jwtProvider.createToken(usrNm);
			result.put("code", "01");
			result.put("accessToken", accessToken);
			return true;
		}

		return false;
	}

	public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if ("".equals(jwtToken) || jwtToken == null || !jwtToken.startsWith("Bearer")) {
				// refresh 토큰 유효 체크
				String redisRefreshToken = jwtProvider.getStoredRefreshToken();
				String usrId = jwtProvider.getStoredRefreshTokenUsrId(redisRefreshToken);
				// DB refresh 토큰
//				String logTp = map.get("logTp");
				Optional<AccountEntity> accountEntity = accountRepository.findByUsrIdAndLogTp(usrId, "A");
				String dbRefreshToken = accountEntity.get().getRefreshToken();
				// 아직 유효 하다면 세션 유지
				if (dbRefreshToken.equals(redisRefreshToken)) {

					String usrNm = accountEntity.get().getUsrNm();
					String accessToken = jwtProvider.createToken(usrNm);
					result.put("code", "01");
					result.put("Authorization", "Bearer " + accessToken);
					return result;
				}
			}

		} catch (JWTVerificationException e) {
			// JWT 검증에 실패한 경우 예외 처리
			handleJwtVerificationException(response, e);
		}
		String usrId = jwtProvider.validateToken(jwtToken);
		Optional<AccountEntity> accountEntity = accountRepository.findByUsrIdAndLogTp(usrId, "A");
		PrincipalDetails principalDetails = new PrincipalDetails(accountEntity.get());
		
		//Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
		Authentication authentication = 
				new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
		//강제로 시큐리티의 세션에 접근하여 authentication 객체를 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return result;
	}

	private void handleJwtVerificationException(HttpServletResponse response, JWTVerificationException e)
			throws IOException {
		final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
		ResultVO resultVo = new ResultVO();
		ObjectMapper objectMapper = new ObjectMapper();

		if (e instanceof TokenExpiredException) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setCharacterEncoding("UTF-8");
			resultVo.setCode("2");
			resultVo.setMsg("로그인 해주세요.");
			String jsonResponse = objectMapper.writeValueAsString(resultVo);
			response.setContentType("text/plain; charset=UTF-8");
			response.getWriter().write(jsonResponse);
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resultVo.setCode("1");
			resultVo.setMsg("로그인 해주세요.");
			String jsonResponse = objectMapper.writeValueAsString(resultVo);
			response.getWriter().write(jsonResponse);
		}
		if (log.isDebugEnabled()) {
			log.debug(String.format("exception: %s, message: %s", e.getClass().getName(), e.getMessage()));
		}
	}
}
