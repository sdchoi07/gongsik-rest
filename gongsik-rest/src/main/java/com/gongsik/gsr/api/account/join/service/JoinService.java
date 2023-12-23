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
			List<InternationalPhoneNumberEntity> listEntity=  internationalPhoneNumberRepository.findAll();
			List<JoinDto> joinList =  listEntity.stream()
											   .map(mapEntity -> {
												JoinDto dto = new JoinDto();
											    dto.setCountryEnNm(mapEntity.getCountryEnNm());
											    dto.setCountryId(mapEntity.getCountryId());
											    dto.setCountryKoNm(mapEntity.getCountryKoNm());
											    dto.setCountryEnNm(mapEntity.getCountryEnNm());
											    dto.setCrtDt(mapEntity.getCrtDt());
											    dto.setExpireDt(mapEntity.getExpireDt());
											    	return dto;
												   
											   }).collect(Collectors.toList());
		return  joinList;
	}

}
