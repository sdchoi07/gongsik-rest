package com.gongsik.gsr.global.jwt;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

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
	
	
	public boolean validateToken(String token) {
        try {
            // refreshToken 유효성 확인
        	String JwtToken = token.replace("Bearer ", "");
			
			String username = JWT.require(Algorithm.HMAC512("gongsik")).build().verify(JwtToken).getClaim("username").asString();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	public String getStoredRefreshToken() {
        return redisTemplate.opsForValue().get("refreshToken");
    }

}
