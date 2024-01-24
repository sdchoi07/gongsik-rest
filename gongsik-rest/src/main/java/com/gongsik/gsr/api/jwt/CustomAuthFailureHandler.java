package com.gongsik.gsr.api.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	/*
	 * HttpServletRequest : request 정보
	 * HttpServletResponse : Response에 대해 설정할 수 있는 변수
	 * AuthenticationException : 로그인 실패 시 예외에 대한 정보
	 */
	
	   @Override
	    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, IOException {
	        // 실패 메세지를 담기 위한 세션 선언
	        HttpSession session = request.getSession();
	        // 세션에 실패 메세지 담기
	        session.setAttribute("loginErrorMessage", exception.getMessage());
	        // 로그인 실패시 이동할 페이지
	        setDefaultFailureUrl("/account/login/?error=true&t=h");
	        // 로그인 실패시 이동할 페이지로 이동
	        super.onAuthenticationFailure(request, response, exception);

	   }
	   }
