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
	
	@Query(value = "SELECT 	"
			+ "			    ITEM_NM                                             "
			+ "            ,ITEM_CNT											"
			+ "            ,ORDER_DT                                            "
			+ "            ,ITEM_IMG										    "
			+ "			   ,CASE WHEN ORDER_ST = '01' THEN '주문준비'               "
			+ "			         WHEN ORDER_ST = '02' THEN '주문완료'               "
			+ "			         ELSE '주문 취소'         END AS ORDER_ST_NM	     "
			+ "            ,ORDER_ST                                             "
			+ "      FROM GS_ORDER_INF a								         "
			+ "     WHERE USR_ID = :usrId										 "
			+ "       AND ORDER_DT  >= :orderDt									 "
														, nativeQuery = true)
	Page<Object[]> findByUsrIdAndOrderDt(@Param("usrId")String usrId, @Param("orderDt")String orderDt, Pageable pageable);
	
	@Query(value=
		      "	SELECT IFNULL(COUNT(*),0) AS COUNT  "
			+ "	FROM GS_ORDER_INF A				    "
			+ " WHERE USR_ID = :usrId 				"
																															,nativeQuery=true)
	int findByUsrId(@Param("usrId")String usrId);




}
