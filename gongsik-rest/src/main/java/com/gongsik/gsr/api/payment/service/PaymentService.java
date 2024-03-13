package com.gongsik.gsr.api.payment.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.account.join.repository.AccountRepository;

@Service
public class PaymentService {

	@Autowired
	AccountRepository accountRepository;
	
	/* 결제자 정보 */
	public Map<String, Object> accountInfo(String usrId) {
		Map<String, Object> map = new HashMap<>();
		
		Optional<Object[]> accountInfo = accountRepository.findByUsrId2(usrId);
		if(accountInfo.isPresent()) {
			System.out.println("?? " + accountInfo.get().toString());
			Object selectOne = accountInfo.get();
			System.out.println("selct : " + selectOne.toString());
			map.put("result", selectOne);
			map.put("code", "success");
		}else {
			map.put("code", "fail");
		}
		return map;
	}

}
