package com.gongsik.gsr.api.account.login.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.global.vo.ResultVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	/* 회원 로그인 */
	public Map<String, Object> accountData(String usrId, String refreshToken) {
		Map<String, Object> map = new HashMap<String, Object>();
		//로그인시 해당 계정 로그인 시간 업데이트
		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatterDate = date.format(formatter);
		
		Optional<AccountEntity> list = accountRepository.findByUsrId(usrId);
		if(list.isPresent()) {
			list.get().setUsrLogInDt(formatterDate);
			accountRepository.save(list.get());
		}
		map.put("result",list);
		return map;
	}

	public void logout(Map<String, String> map) {
		Date now = new Date(System.currentTimeMillis()+ (600000*10));
		String refreshToken = map.get("refreshToken");
		
		Optional<AccountEntity> list = accountRepository.findByUsrId(map.get("usrId"));
		if(!list.isPresent()) {
                new LoginException();
		}
        //**로그아웃 구분하기 위해 redis에 저장**
		redisTemplate.opsForValue().set("logout", refreshToken, Duration.ofMillis(now.getTime()));
	}

	public ResultVO SNSLogin(Map<String, String> map) {
		ResultVO resultVo = new ResultVO();
		
		String provider = map.get("provider");
		String providerId = map.get("providerId");
		String email = map.get("email");
		String role = map.get("role");
		String logTp = map.get("logTp");
		
		Optional<AccountEntity> accountEntity = accountRepository.findByUsrIdAndProviderIdAndLogTp(email, providerId, logTp);
		AccountEntity result = new AccountEntity();
		if(accountEntity.isEmpty()) {
			result.setUsrId(email);
			result.setProvider(provider);
			result.setProviderId(providerId);
			result.setUsrRole(role);
			result.setLogTp(logTp);
			accountRepository.save(result);
		}else {
			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formatterDate = date.format(formatter);
			result.setUsrLogInDt(formatterDate);
			accountRepository.save(result);
		}
		
		
		return resultVo;
	}
	

}
