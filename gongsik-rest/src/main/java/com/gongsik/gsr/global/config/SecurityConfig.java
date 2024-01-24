package com.gongsik.gsr.global.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.jwt.PrincipalService;
import com.gongsik.gsr.global.jwt.JwtAuthenticationFilter;
import com.gongsik.gsr.global.jwt.JwtAuthorizationFilter;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
		//private PrincipalAuthenticatiorProvider principalAuthenticatiorProvider;
		@Autowired
		
		private AccountRepository accountRepository;
		
		
		
		//해당 메서드의 리턴되는 오브젝트를 ioC로 등록해줌.
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
				AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
		        AuthenticationManager authenticationManager = sharedObject.build();
		        JwtAuthenticationFilter jwtAuthenticationFilter 
                = new JwtAuthenticationFilter(authenticationManager);
		        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
		        http.authenticationManager(authenticationManager);

	        
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
			.addFilter(jwtAuthenticationFilter)
			.addFilter(new JwtAuthenticationFilter(authenticationManager))
			.addFilter(new JwtAuthorizationFilter(authenticationManager,accountRepository))
			.authorizeHttpRequests((authorizeRequests) ->
					authorizeRequests
							.requestMatchers("/api/login").permitAll()
							.requestMatchers("/api/account/test/").authenticated()
							.requestMatchers("/api/account/admin").hasAuthority("USER")
							.requestMatchers("/user/**", "/api/v1/posts/**").hasRole("USER")
							.anyRequest().permitAll()
			);
//			.((request, response, authentication) -> {
//                // 로그인 성공 시 사용자 정보를 추출하여 JSON 응답을 생성하여 클라이언트에 전송
//                response.setContentType("application/json");
//                response.setCharacterEncoding("UTF-8");
//                
//                Map<String, Object> responseData = new HashMap<>();
//                responseData.put("success", true);
//                responseData.put("redirect", "/");
//                
//                // 로그인한 사용자의 이름과 역할(role) 정보 추출하여 응답에 포함
//                responseData.put("username", authentication.getName());
//                responseData.put("role", authentication.getAuthorities());
//                
//                List<String> roles = authentication.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.toList());
//                
//                responseData.put("roles", roles);
//                
//                // JSON 형태로 변환하여 응답에 쓰기
//                ObjectMapper objectMapper = new ObjectMapper();
//                String jsonResponse = objectMapper.writeValueAsString(responseData);
//                response.getWriter().write(jsonResponse);
//            });
			
		    return http.build();
		}
}
