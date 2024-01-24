package com.gongsik.gsr.api.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PrincipalAuthenticatiorProvider  implements AuthenticationProvider {

    @Autowired
    private PrincipalService principalService;
    
    public PrincipalAuthenticatiorProvider(PrincipalService principalService) {		
		this.principalService = principalService;
	}
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName(); // 사용자가 입력한 id
        String password = (String) authentication.getCredentials(); // 사용자가 입력한 password

        // 생성해둔 MemberPrincipalDetailService 에서 loadUserByUsername 메소드를 호출하여 사용자 정보를 가져온다.
        PrincipalDetails principalDetails = (PrincipalDetails) principalService.loadUserByUsername(username);

        // ====================================== 비밀번호 비교 ======================================
        // 사용자가 입력한 password 와 DB 에 저장된 password 를 비교한다.

        // db 에 저장된 password
        String dbPassword = principalDetails.getPassword();
        // 암호화 방식 (BCryptPasswordEncoder) 를 사용하여 비밀번호를 비교한다.
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        if(!passwordEncoder.matches(password, dbPassword)) {
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        // ========================================================================================
        
        // STEP3. ROLE권한 설정
 		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
 		roles.add(new SimpleGrantedAuthority(principalDetails.getAuthorities().toString()));
 		

        // 인증이 성공하면 UsernamePasswordAuthenticationToken 객체를 반환한다.
        // 해당 객체는 Authentication 객체로 추후 인증이 끝나고 SecurityContextHolder.getContext() 에 저장된다.
        return new UsernamePasswordAuthenticationToken(username, password, roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}