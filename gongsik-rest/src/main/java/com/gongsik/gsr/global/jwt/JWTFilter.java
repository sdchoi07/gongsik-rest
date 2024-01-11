package com.gongsik.gsr.global.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("필터");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		//토큰 : exam 이걸 만들줘야하는데, id,pw 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답을 해준다.
		//요청할떄마다 header 에 authorization 에 value  값으로 토큰을 가지고 옴
		//그떄 토큰이 넘오면 이 토큰이 내가 만든 토큰이 맞는지 검증하면 됨(RSA,HS256)
		if(req.getMethod().equals("POST")) {
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			
			if(headerAuth.equals("exam")) {
				chain.doFilter(req, res);
			}else {
				PrintWriter out = res.getWriter();
				out.print("인증안됨");
			}
		}
	}

}
