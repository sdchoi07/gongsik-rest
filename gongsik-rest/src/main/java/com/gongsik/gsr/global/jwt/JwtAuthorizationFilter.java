package com.gongsik.gsr.global.jwt;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.account.join.service.JoinService;
import com.gongsik.gsr.api.jwt.PrincipalDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//시큐리티가 filter가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
//권한이나 인증이 필요한 특정주소를 요청했을 때 위 필터를 무조건 타게 되었음.
//만약에 권한이 인증이 필요한 주소가 아니라면 이 필터 사용 안함
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	
	private AccountRepository accountRepository;

	
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AccountRepository accountRepository) {
		super(authenticationManager);
		this.accountRepository = accountRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
					throws IOException, ServletException{
		//super.doFilterInternal(request, response, chain);
		String jwtHeader = request.getHeader("Authorization");
		//jwt토큰을 거증해서 정상적인 사용자인지 화인
		if("".equals(jwtHeader) || jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}
		
		//jwt토큰을 검증을해서 정상적인 사용자인지 확인
		 try {
				String JwtToken = request.getHeader("Authorization").replace("Bearer ", "");
				
				String username = JWT.require(Algorithm.HMAC512("gongsik")).build().verify(JwtToken).getClaim("username").asString();
				if(!"".equals(username) || !username.equals(null)) {
					Optional<AccountEntity> account = accountRepository.findByUsrId(username);
					if(account.isPresent()) {
						AccountEntity result = account.get();
					
						PrincipalDetails principalDetails = new PrincipalDetails(result);
						
						//Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
						Authentication authentication = 
								new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
						//강제로 시큐리티의 세션에 접근하여 authentication 객체를 저장
						SecurityContextHolder.getContext().setAuthentication(authentication);
						
						chain.doFilter(request, response);
					}
				}
	        } catch (JWTVerificationException e) {
	            // JWT 검증에 실패한 경우 예외 처리
	            handleJwtVerificationException(response, e);
	        }
		
	}
	 private void handleJwtVerificationException(HttpServletResponse response, JWTVerificationException e) throws IOException {
		 	final Logger log =LoggerFactory.getLogger(JwtAuthorizationFilter.class);
		 	if (log.isDebugEnabled()) {
		        log.debug(String.format("exception: %s, message: %s", e.getClass().getName(), e.getMessage()));
		    }

//	        // 예외에 따른 응답 처리
//	        if (e instanceof SignatureVerificationException) {
//	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	            response.getWriter().write("Invalid signature in JWT token.");
//	        } else if (e instanceof TokenExpiredException) {
//	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	            response.getWriter().write("JWT token has expired.");
//	        } else if (e instanceof UnsupportedJwtException) {
//	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	            response.getWriter().write("Unsupported JWT token.");
//	        } else if (e instanceof MalformedJwtException) {
//	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	            response.getWriter().write("Malformed JWT token.");
//	        } else if (e instanceof IllegalArgumentException) {
//	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	            response.getWriter().write("Invalid JWT token.");
//	        } else {
//	            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//	            response.getWriter().write("Authentication failed.");
//	        }
//	    }
	 }
 }
