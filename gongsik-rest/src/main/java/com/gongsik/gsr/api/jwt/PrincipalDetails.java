package com.gongsik.gsr.api.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;
//시큐리티가 login 주소 요청이 오면 낚아 채서 로그인을 진행시킨다.
//로그인 진행이 완료가 되면 시큐리티 session을 만든다.(Security ContextHolder)
//오브젝트 타입 - > Authentication 타입 객체
//Authentication 안에 User 정보가 있어야함
//User 오브젝트 타입 - > UserDetails 타입 객체
//Security Session -> Authntication -> UserDetails(PrincipalDetails)
public class PrincipalDetails implements UserDetails{
	
	@Autowired
	private AccountEntity accountEntity;
	
	

	public PrincipalDetails(AccountEntity accountEntity) {
		this.accountEntity = accountEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		if("ADMIN".equals(accountEntity.getUsrRole())) {
			collect.add(new SimpleGrantedAuthority("ADMIN"));
		}else {
			collect.add(new SimpleGrantedAuthority("USER"));
		}
		
		//collect.add(() -> accountEntity.getUsrRole());
	    return collect;
	}

	@Override
	public String getPassword() {
		return accountEntity.getUsrPwd();
	}

	@Override
	public String getUsername() {
		return accountEntity.getUsrId();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		//1년 동안 로그인 안할시 휴면 계정 변환
		//현재시간 - 마지막 로그인시간
		return true;
	}

}
