package com.gongsik.gsr.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
		
		//해당 메서드의 리턴되는 오브젝트를 ioC로 등록해줌.
		@Bean
		public BCryptPasswordEncoder encodePwd() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
			http
			.csrf((csrfConfig) ->
					csrfConfig.disable()
			)
			.headers((headerConfig) ->
					headerConfig.frameOptions(frameOptionsConfig ->
							frameOptionsConfig.disable()
					)
			);
		    return http.build();
		}
}
