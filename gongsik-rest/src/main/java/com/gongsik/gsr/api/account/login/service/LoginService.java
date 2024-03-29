package com.gongsik.gsr.api.account.login.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.jwt.PrincipalDetails;
import com.gongsik.gsr.global.jwt.JwtProvider;
import com.gongsik.gsr.global.vo.ResultVO;

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
	
	/* 회원 로그인 */
	public Map<String, Object> accountData(String usrId) {
		Map<String, Object> map = new HashMap<String, Object>();
		//로그인시 해당 계정 로그인 시간 업데이트
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatterDate = date.format(formatter);
		
		String refesthToken = redisTemplate.opsForValue().get("refreshToken");
		Optional<AccountEntity> list = accountRepository.findByUsrId(usrId);
		if(list.isPresent()) {
			list.get().setUsrLogInDt(formatterDate);
			list.get().setRefreshToken(refesthToken);
			accountRepository.save(list.get());
		}
		map.put("usrId",list.get().getUsrId());
		map.put("logTp", list.get().getLogTp());
		map.put("usrRole", list.get().getUsrRole());
		return map;
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
		Date now = new Date(System.currentTimeMillis()+ (600000*10));
		String refreshToken = map.get("refreshToken");
		
		Optional<AccountEntity> list = accountRepository.findByUsrIdAndLogTp(usrId,logTp);
		if(!list.isPresent()) {
                new LoginException();
		}
        //**로그아웃 구분하기 위해 redis에 저장**
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

}
