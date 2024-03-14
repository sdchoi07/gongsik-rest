package com.gongsik.gsr.api.main.categories.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.main.categories.entity.CategoriesEntity;
import com.gongsik.gsr.api.payment.dto.InvenDto;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long>{

	@Query(value=
		      "	SELECT A.INVEN_S_CLS_NM, A.INVEN_S_CLS_NO, D.PRODUCT_PRICE, D.PRODUCT_SALES_CNT, A.INVEN_CNT, D.PRODUCT_URL,  F.DEL_YN ,F.USE_YN "
			+ "	FROM GS_INVENTORY_MST A																											 "
			+ "	JOIN GS_PRODUCT_INF D ON A.INVEN_S_CLS_NO = D.PRODUCT_NO																         "
			+ "	LEFT JOIN (SELECT A.DEL_YN , A.USE_YN , A.CART_ITEM_NO, A.CART_USR_ID															 "
			+ "			FROM GS_CART_INF A																									     "
			+ "			JOIN GS_ACCOUNT_INF B ON A.CART_USR_ID = B.USR_ID AND USR_ID = :usrId													 "
			+ "			WHERE A.USE_YN = 'Y' AND A.DEL_YN = 'N'  AND A.CART_ST != 'W') F ON F.CART_ITEM_NO = A.INVEN_S_CLS_NO	 									 "
			+ "	WHERE INVEN_M_CLS_NO = :itemNo 																									 "
			+ "	AND CRG_DATE <= :cur 																										     "
			+ "	AND END_DATE = :endDate 																										 "
																															,nativeQuery=true)
	List<Object[]> findAllProduct(@Param("usrId")String usrId, @Param("itemNo")String itemNo, @Param("cur")String cur, @Param("endDate")String endDate, Pageable pageable);
	
	@Query(value=
		      "	SELECT A.INVEN_S_CLS_NM, A.INVEN_S_CLS_NO, D.SEED_PRICE, D.SEED_SALES_CNT, A.INVEN_CNT, D.SEED_URL,  F.DEL_YN ,F.USE_YN "
			+ "	FROM GS_INVENTORY_MST A																									"
			+ "	JOIN GS_SEED_INF D ON A.INVEN_S_CLS_NO = D.SEED_NO																	    "
			+ "	LEFT JOIN (SELECT A.DEL_YN , A.USE_YN , A.CART_ITEM_NO, A.CART_USR_ID															 "
			+ "			FROM GS_CART_INF A																									     "
			+ "			JOIN GS_ACCOUNT_INF B ON A.CART_USR_ID = B.USR_ID AND USR_ID = :usrId													 "
			+ "			WHERE A.USE_YN = 'Y' AND A.DEL_YN = 'N' AND A.CART_ST != 'W') F ON F.CART_ITEM_NO = A.INVEN_S_CLS_NO	 									 "
			+ "	WHERE INVEN_M_CLS_NO = :itemNo 																					        "
			+ "	AND CRG_DATE <= :cur 																								    "
			+ "	AND END_DATE = :endDate 																								"
																															,nativeQuery=true)
	List<Object[]> findAllSeed(@Param("usrId")String usrId, @Param("itemNo")String itemNo, @Param("cur")String cur, @Param("endDate")String endDate, Pageable pageable);

	@Query(value=
		      "	SELECT A.INVEN_S_CLS_NM, A.INVEN_S_CLS_NO, D.CHEMISTRY_PRICE, D.CHEMISTRY_SALES_CNT, A.INVEN_CNT, D.CHEMISTRY_URL,  F.DEL_YN ,F.USE_YN "
			+ "	FROM GS_INVENTORY_MST A																												   "
			+ "	JOIN GS_CHEMISTRY_INF D ON A.INVEN_S_CLS_NO = D.CHEMISTRY_NO																		   "
			+ "	LEFT JOIN (SELECT A.DEL_YN , A.USE_YN , A.CART_ITEM_NO, A.CART_USR_ID															 "
			+ "			FROM GS_CART_INF A																									     "
			+ "			JOIN GS_ACCOUNT_INF B ON A.CART_USR_ID = B.USR_ID AND USR_ID = :usrId													 "
			+ "			WHERE A.USE_YN = 'Y' AND A.DEL_YN = 'N' AND A.CART_ST != 'W') F ON F.CART_ITEM_NO = A.INVEN_S_CLS_NO	 									 "
			+ "	WHERE INVEN_M_CLS_NO = :itemNo 																									       "
			+ "	AND CRG_DATE <= :cur 																												   "
			+ "	AND END_DATE = :endDate 																										       "
																															,nativeQuery=true)
	List<Object[]> findAllChemistry(@Param("usrId")String usrId, @Param("itemNo")String itemNo, @Param("cur")String cur, @Param("endDate")String endDate, Pageable pageable);

	Optional<CategoriesEntity> findByInvenSClsNo(String itemKey);
	
	@Query(value=
		      "			SELECT   A.INVEN_S_CLS_NM  AS ITEM_NM																"
		      + "				,A.INVEN_S_CLS_NO  AS ITEM_NO																"
		      + "				,A.INVEN_CNT 	   AS INVEN_CNT																"
		      + "				,CASE WHEN B.CHEMISTRY_PRICE  IS NOT NULL THEN B.CHEMISTRY_PRICE 							"
		      + "		      		WHEN C.PRODUCT_PRICE IS NOT NULL THEN C.PRODUCT_PRICE									"
		      + "		      		WHEN D.SEED_PRICE IS NOT NULL THEN D.SEED_PRICE ELSE 0 END AS ITEM_PRICE				"
		      + "		FROM GS_INVENTORY_MST A																				"
		      + "		LEFT JOIN GS_CHEMISTRY_INF B ON A.INVEN_S_CLS_NO  = B.CHEMISTRY_NO 									"
		      + "		LEFT JOIN GS_PRODUCT_INF C ON A.INVEN_S_CLS_NO = C.PRODUCT_NO 										"
		      + "		LEFT JOIN GS_SEED_INF D ON A.INVEN_S_CLS_NO = D.SEED_NO 											"
		      + "		WHERE A.INVEN_S_CLS_NO IN (SELECT SUBSTRING_INDEX(SUBSTRING_INDEX( :itemNo, ',', n), ',', -1) AS part "
		      + "											FROM (SELECT 1 AS n UNION SELECT 2) AS numbers)	  	 		  "
																															,nativeQuery=true)
	List<InvenDto> findByInvneSClsNo(@Param("itemNo")String itemNo);
	
	@Modifying
	@Query(value=
		      "			UPDATE  GS_INVENTORY_MST 			"
		      + "		   SET INVEN_CNT  = :invenCnt		"
		      + "        WHERE INVEN_S_CLS_NO :itemNo  		"
																															,nativeQuery=true)
	void updateItemCnt(@Param("itemNo")String itemNo, @Param("invenCnt")int invenCnt);
}
