package com.gongsik.gsr.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gongsik.gsr.global.jwt.JWTFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<JWTFilter> filter(){
		FilterRegistrationBean<JWTFilter> bean = new FilterRegistrationBean<>(new JWTFilter());
		bean.addUrlPatterns("/api/**");
		bean.setOrder(0);
		return bean;
	}
}
