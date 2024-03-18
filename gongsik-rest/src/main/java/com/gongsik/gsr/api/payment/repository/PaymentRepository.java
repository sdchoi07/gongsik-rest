package com.gongsik.gsr.api.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.payment.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long>{
	
	@Query(value=""
			+ "		SELECT COALESCE(SUM(PAYMENT_AMOUNT), 0) PAYMENT_AMOUNT  "
			+ "		  FROM GS_PAYMENT_INF   							 "
			+ "		 WHERE PAYMENT_USR_NM = :usrNm 						 "
			+ "		   AND PAYMENT_USR_ID = :usrId 						 "
			+ "		   AND PAYMENT_ST = '02'							 " ,nativeQuery= true)
	int findByPaymentUsrIdAndPaymentUsrNm(@Param("usrId")String usrId, @Param("usrNm")String usrNm);

//	Optional<List<Object[]>> findByUsrId2(String usrId);

}
