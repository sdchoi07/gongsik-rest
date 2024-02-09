package com.gongsik.gsr.api.mypage.delivery.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.account.join.entity.QAccountEntity;
import com.gongsik.gsr.api.mypage.delivery.dto.DeliveryDto;
import com.gongsik.gsr.api.mypage.delivery.entity.QDeliveryEntity;
import com.gongsik.gsr.api.mypage.delivery.repository.DeliveryRepository;
import com.querydsl.core.Tuple;
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
	
	public Map<String, Object> orderList(String usrId) {
		QDeliveryEntity qDeliveryEntity = QDeliveryEntity.deliveryEntity;
		QAccountEntity qAccountEntity = QAccountEntity.accountEntity;
		JPAQueryFactory query = new JPAQueryFactory(em);
		List<Tuple> list = query.select(qDeliveryEntity.delvAreaNm, qDeliveryEntity.delvAreaAddr, qDeliveryEntity.delvAreaNo, qDeliveryEntity.useYn)
							.from(qDeliveryEntity)
							.join(qAccountEntity).on(qAccountEntity.usrNm.eq(qDeliveryEntity.delvUsrNm), qAccountEntity.usrId.eq(qDeliveryEntity.delvUsrId))
							.where(qDeliveryEntity.delvUsrId.eq(usrId), qDeliveryEntity.useYn.eq("Y"), qDeliveryEntity.delYn.eq("N")).fetch();
		log.info("result : {} \n" , list);

		
		return null;
	}

}
