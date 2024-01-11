package com.gongsik.gsr.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.gongsik.gsr.api.auth.PrincipalAuthenticatiorProvider;
import com.gongsik.gsr.global.jwt.JWTFilter;
import com.gongsik.gsr.global.jwt.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
		@Autowired
		private PrincipalAuthenticatiorProvider principalAuthenticatiorProvider;
		
		@Autowired
		public void configure (AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(principalAuthenticatiorProvider);
		}
		
		//해당 메서드의 리턴되는 오브젝트를 ioC로 등록해줌.
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
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
			)
			.sessionManagement( //session 사용안함
						(sessionManagement) ->
							sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
			.formLogin((formLogin)->
						formLogin.disable()
			)
			.httpBasic((httpBasic)->
						httpBasic.disable()
			)
			.addFilter(new JwtAuthenticationFilter(null))
			.authorizeHttpRequests((authorizeRequests) ->
					authorizeRequests
							.requestMatchers("/admin/**", "/api/admins/**").hasRole("ADMIN")
							.requestMatchers("/user/**", "/api/v1/posts/**").hasRole("USER")
							.anyRequest().permitAll()
			);
//			.formLogin((formLogin) ->
//			formLogin
//					.loginPage("/account/join") 
//					//.usernameParameter("username")
//					.loginProcessingUrl("/api/account/login") // login주소가 호출이되며 시큐리티가 낚아채서 대신 로그인 진행해줌.
//					.successHandler((request, response, authentication) -> {
//	                    // 로그인 성공 시 사용자 정보를 추출하여 JSON 응답을 생성하여 클라이언트에 전송
//	                    response.setContentType("application/json");
//	                    response.setCharacterEncoding("UTF-8");
//	                    
//	                    Map<String, Object> responseData = new HashMap<>();
//	                    responseData.put("success", true);
//	                    responseData.put("redirect", "/");
//	                    
//	                    // 로그인한 사용자의 이름과 역할(role) 정보 추출하여 응답에 포함
//	                    responseData.put("username", authentication.getName());
//	                    responseData.put("role", authentication.getAuthorities());
//	                    
//	                    List<String> roles = authentication.getAuthorities().stream()
//	                        .map(GrantedAuthority::getAuthority)
//	                        .collect(Collectors.toList());
//	                    
//	                    responseData.put("roles", roles);
//	                    
//	                    // JSON 형태로 변환하여 응답에 쓰기
//	                    ObjectMapper objectMapper = new ObjectMapper();
//	                    String jsonResponse = objectMapper.writeValueAsString(responseData);
//	                    response.getWriter().write(jsonResponse);
//	                })
//					.failureHandler(new CustomAuthFailureHandler())
//			);
		    return http.build();
		}
	    

}
