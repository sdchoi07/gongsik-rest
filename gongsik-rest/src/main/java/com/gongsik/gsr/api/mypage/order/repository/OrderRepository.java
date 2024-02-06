package com.gongsik.gsr.api.mypage.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.order.dto.OrderDto;
import com.gongsik.gsr.api.mypage.order.entity.OrderEntity;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>{
	
	@Query(value = "SELECT  new com.gongsik.gsr.api.mypage.order.dto.OrderDto	"
			+ "			  (  a.itemNm                                              "
			+ "            ,a.itemCnt											 "
			+ "            ,CASE WHEN a.orderSt = '01' THEN '주문준비'               "
			+ "                  WHEN a.orderSt = '02' THEN '주문완료'               "
			+ "                  ELSE '주문 취소'         END AS orderSt)		     "
			+ "      FROM OrderEntity a								     "
			+ "     WHERE  a.usrId = :usrId										 "
			+ "       AND a.orderDt  <= :orderDt									 "
														)
	Page<OrderDto> findByUsrNmAndUsrIdAndOrderDt(@Param("usrId")String usrId, @Param("orderDt")String orderDt, Pageable pageable);




}
