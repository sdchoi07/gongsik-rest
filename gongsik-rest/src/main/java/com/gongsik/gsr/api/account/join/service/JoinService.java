package com.gongsik.gsr.api.account.join.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.join.entity.InternationalPhoneNumberEntity;
import com.gongsik.gsr.api.account.join.repository.InternationalPhoneNumberRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Tag(name = "Menu Controller", description = "회원가입")
public class JoinService {
	
	@Autowired
	private InternationalPhoneNumberRepository internationalPhoneNumberRepository;
	
	public List<JoinDto> countryPhNmList() {
		    //조회된 국제번호를 String형 list에 저장
			List<JoinDto> list=  internationalPhoneNumberRepository.findAllOnlyCountryPhNm();
			//저장된 listEntity를 dto 하나씩 저장하여 joinList에 저장
//			List<JoinDto> joinList =  listEntity.stream()
//											   .map(mapEntity -> { 
//												JoinDto dto = new JoinDto(); //entitiy에 저장한 data 를 dto에 저장
//											    dto.setCountryId(mapEntity.getCountryId());
//											    	return dto;
//												   
//											   }).collect(Collectors.toList()); //각각 저장된 dto는 list에 담기 
		return  list;
	}

}
