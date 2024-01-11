package com.gongsik.gsr.api.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
//시큐리티 설정에서 loginProcessingUrl("/api/accunt/login")
///login 요청이 오면 자동으로 UserDetialsService 타입으로 IoC되어있는 loadUserByUsername 함수가실행

@Service
public class PrincipalService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AccountEntity> accountEntity = accountRepository.findByUsrId(username);

        if (accountEntity.isPresent()) {
            AccountEntity result = accountEntity.get();
            System.out.println(result);
            return new PrincipalDetails(result);
        }

        throw new UsernameNotFoundException("해당 계정으로 로그인 실패: " + username);
    }
}
