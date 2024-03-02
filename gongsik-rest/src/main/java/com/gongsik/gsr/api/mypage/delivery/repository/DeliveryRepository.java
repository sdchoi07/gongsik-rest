package com.gongsik.gsr.api.mypage.delivery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryEntity;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long>{


	Optional<DeliveryEntity> findByDelvUsrIdAndDelvAreaNo(String usrId, String zipCode);

	Optional<DeliveryEntity> findByDelvUsrIdAndDelvUsrNmAndDelvUseYn(String usrId, String usrNm, String addressYn);

	Optional<DeliveryEntity> findByDelvUsrIdAndDelvAreaSeq(String usrId, long seq);

	Optional<DeliveryEntity> findByDelvUsrIdAndDelvUseYn(String usrId, String addressYn);

	
	@Query(value=
		      "	SELECT IFNULL(COUNT(*),0) AS COUNT  "
			+ "	FROM GS_DELV_AREA_INF A				    "
			+ " WHERE DELV_USR_ID = :usrId 				"
			+ "   AND  USE_YN = 'Y'                     "
			+ "   AND  DEL_YN = 'N'                     "
																															,nativeQuery=true)
	int findByDelvUsrId(@Param("usrId")String usrId);


}
