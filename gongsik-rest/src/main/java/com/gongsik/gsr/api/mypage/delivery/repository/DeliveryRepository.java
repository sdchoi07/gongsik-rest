package com.gongsik.gsr.api.mypage.delivery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryEntity;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long>{


	Optional<DeliveryEntity> findByDelvUsrIdAndDelvAreaNo(String usrId, String zipCode);

	Optional<DeliveryEntity> findByDelvUsrIdAndDelvUsrNmAndDelvUseYn(String usrId, String usrNm, String addressYn);

	Optional<DeliveryEntity> findByDelvUsrIdAndDelvAreaSeq(String usrId, long seq);

	Optional<DeliveryEntity> findByDelvUsrIdAndDelvUseYn(String usrId, String addressYn);

}
