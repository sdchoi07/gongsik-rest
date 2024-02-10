package com.gongsik.gsr.api.mypage.delivery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.entity.QAccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.mypage.delivery.dto.DeliveryDto;
import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryEntity;
import com.gongsik.gsr.api.mypage.delivery.entity.QDeliveryEntity;
import com.gongsik.gsr.api.mypage.delivery.repository.DeliveryRepository;
import com.gongsik.gsr.global.vo.ResultVO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryService {
	
	@Autowired
	EntityManager em;
	
	@Autowired
	private DeliveryRepository deliveryRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	
	/* 해당 유저 배송지역 조회 */
	public Map<String, Object> delvList(String usrId) {
		Map<String, Object> map = new HashMap<>();
		QDeliveryEntity qDeliveryEntity = QDeliveryEntity.deliveryEntity;
		QAccountEntity qAccountEntity = QAccountEntity.accountEntity;
		JPAQueryFactory query = new JPAQueryFactory(em);
		List<Tuple> list = query.select(qDeliveryEntity.delvAreaNm, qDeliveryEntity.delvAreaAddr, qDeliveryEntity.delvAreaNo, qDeliveryEntity.delvPhNo
										,qDeliveryEntity.delvUseYn
										, new CaseBuilder().when(qDeliveryEntity.delvUseYn.eq("Y")).then("기본배송지")
										                    .otherwise("").as("delvUseYnNm"))
							.from(qDeliveryEntity)
							.join(qAccountEntity).on(qAccountEntity.usrNm.eq(qDeliveryEntity.delvUsrNm), qAccountEntity.usrId.eq(qDeliveryEntity.delvUsrId))
							.where(qDeliveryEntity.delvUsrId.eq(usrId), qDeliveryEntity.useYn.eq("Y"), qDeliveryEntity.delYn.eq("N"))
							.orderBy(qDeliveryEntity.delvUseYn.desc())
							.fetch();
		List<DeliveryDto> result = new ArrayList<>();
		for(Tuple tuple : list) {
			DeliveryDto dto = new DeliveryDto();
			dto.setDelvAreaAddr(tuple.get(qDeliveryEntity.delvAreaAddr));
			dto.setDelvAreaNm(tuple.get(qDeliveryEntity.delvAreaNm));
			dto.setDelvAreaNo(tuple.get(qDeliveryEntity.delvAreaNo));
			dto.setUsrPhone(tuple.get(qAccountEntity.usrPhone));
			dto.setDelvUseYn(tuple.get(qDeliveryEntity.delvUseYn));
			dto.setDelvPhNo(tuple.get(qDeliveryEntity.delvPhNo));

			result.add(dto);
		}
		map.put("result", result);
		log.info("dto : {} " , result);
		return map;
	}
	
	/* 해당 유저 새 배송지역 추가 */
	public Map<String, Object> saveNewAddress(Map<String, String> request) {
		Map<String, Object> map = new HashMap<>();
		ResultVO resultVo = new ResultVO();
		
		String usrId = request.get("usrId");
		Optional<AccountEntity> account = accountRepository.findByUsrId(usrId);
		String usrNm = "";
		if(account.isPresent()) {
			usrNm = account.get().getUsrNm();
		}
		String addressNm = request.get("addressNm");
		String phoneNumber = request.get("phoneNumber");
		String zipCode = request.get("zipCode");
		String address = request.get("address");
		String addressDetail = request.get("addressDetail");
		String addressYn = request.get("addressYn");
		
		Optional<DeliveryEntity> delivery = deliveryRepository.findByDelvUsrIdAndDelvAreaNo(usrId, zipCode);
		if(delivery.isPresent()) {
			resultVo.setCode("fail");
			resultVo.setMsg("이미 등록된 지역 입니다.");
			map.put("result", resultVo);
			return map;
		}else {
			DeliveryEntity deliveryEntity = new DeliveryEntity();
			deliveryEntity.setDelvAreaAddr(address + addressDetail);
			deliveryEntity.setDelvAreaNm(addressNm);
			deliveryEntity.setDelvAreaNo(zipCode);
			deliveryEntity.setDelvUseYn(addressYn);
			deliveryEntity.setDelvUsrId(usrId);
			deliveryEntity.setDelvUsrNm(usrNm);
			deliveryEntity.setDelvPhNo(phoneNumber);
			
			DeliveryEntity result = deliveryRepository.save(deliveryEntity);
			if(result != null) {
				resultVo.setCode("success");
				resultVo.setMsg("새로운 배송지가 추가 되었습니다.");
				
			}else {
				resultVo.setMsg("");
			}
		}
		map.put("result", resultVo);
		return map;
	}

}
