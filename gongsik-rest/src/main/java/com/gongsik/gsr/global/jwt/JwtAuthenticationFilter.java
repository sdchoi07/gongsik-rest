package com.gongsik.gsr.global.jwt;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.auth.PrincipalDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//스프링시큐리티에서 UsernamePasswordAuthenticationFilterr가 있음.
//login 요청해서 username, password 전송하면(post)
//UsernamePasswordAuthenticationFilter 동작을함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	public final AuthenticationManager authenticationManager;
		
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter:로그인 시도중" );
		//회원정보 받아서 로그인 시도 - PricncipalDetails이 실행을함 
		try {
//			System.out.println(request.getInputStream());
//			BufferedReader br = request.getReader();
//			String input = null;
//			while((input = br.readLine())!=null){
//				System.out.println(br.readLine());
//			}
			ObjectMapper em = new ObjectMapper();
			AccountEntity accountEntity = em.readValue(request.getInputStream(), AccountEntity.class);
			System.out.println(accountEntity);
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(accountEntity.getUsrId(), accountEntity.getUsrPwd());
			//loadUserByUsername()함수가 실행후 인증완료되었다는 의미
			Authentication authentication = 
					authenticationManager.authenticate(authenticationToken);
			
			//authentication 객체가 session영역에 저장을 해야하고 그 방벙비 return해주면됨.
			//리턴이유는 권한관리를 security가 대신 해주기떄문에 편하려고하는거임
			//굳이 JWT토큰을 사용하면서 세션을만들 이유가 없음, 근데 단지 권한처리때문에 session 에 저장
			return authentication;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
		
		
	}
	//attemptAuthentication 실행후 인증이 정상적으로 된후, 함수가 실행
	//JWT 토큰을 만들어서 request요청한 사용자에게 JWT 토큰을 response해줌
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterCahin
			, Authentication authentication)
			throws IOException, ServletException {
		
		//로그인 되었다는 의미
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		System.out.println(principalDetails.getUsername());
		
		//Hash암호방식
		String jwtToken = JWT.create()
						     .withSubject(principalDetails.getUsername())
						     .withExpiresAt(new Date(System.currentTimeMillis()+ (600000*10)))
						     .withClaim("id", principalDetails.getUsername())
							 .withClaim("username", principalDetails.getUsername())
							 .sign(Algorithm.HMAC512("gongsik"));
		
		response.addHeader("Authorization", "Bearer"+jwtToken);
						     
		
	}
}
