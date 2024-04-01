package com.gongsik.gsr.global.jwt;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.global.vo.ResultVO;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtProvider {
	
	private final RedisTemplate<String, String> redisTemplate;
	
	// 인증 정보 조회

	public String createToken(String usrNm) {
		Date now = new Date(System.currentTimeMillis() + (600000 * 10));
		// accestToken Hash암호방식
		String accessToken = JWT.create().withSubject("GON토큰").withExpiresAt(now)
				.withClaim("username", usrNm).sign(Algorithm.HMAC512("gongsik"));
		return accessToken;
	}

	public String refreshToken(String usrNm) {
		Date now = new Date(System.currentTimeMillis() + (1200000 * 10));
		// refreshToken
		String refreshToken = JWT.create().withSubject("GON토큰").withExpiresAt(now)
				.withClaim("username", usrNm).sign(Algorithm.HMAC512("gongsik"));
		redisTemplate.opsForValue().set("refreshToken", refreshToken, Duration.ofMillis(now.getTime()));
		System.out.println("redsi refes : " + redisTemplate.opsForValue().get("refreshToken"));
		return refreshToken;
	}
	
	
	public String validateToken(String token) {
        try {
            // refreshToken 유효성 확인
			
			String username = JWT.require(Algorithm.HMAC512("gongsik")).build().verify(token).getClaim("username").asString();
			
            return username;
        } catch (Exception e) {
            return "";
        }
    }
	
	public String getStoredRefreshToken() {
        return redisTemplate.opsForValue().get("refreshToken");
    }
	
	public String getStoredRefreshTokenUsrId(String refreshToekn) {
        return JWT.require(Algorithm.HMAC512("gongsik")).build().verify(refreshToekn).getClaim("username").asString();
    }

	public Authentication getAuthentication(String usrId, String usrPwd) {
		
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(usrId, usrPwd);
		//loadUserByUsername()함수가 실행후 인증완료되었다는 의미
		Authentication authentication = authenticationToken;
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}


}
