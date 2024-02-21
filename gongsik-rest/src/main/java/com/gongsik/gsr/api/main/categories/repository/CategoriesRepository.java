package com.gongsik.gsr.api.main.categories.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.main.categories.entity.CategoriesEntity;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long>{

	@Query(value=
		      "	SELECT A.INVEN_S_CLS_NM, A.INVEN_S_CLS_NO, D.PRODUCT_PRICE, D.PRODUCT_SALES_CNT, A.INVEN_CNT, D.PRODUCT_URL,  F.DEL_YN ,F.USE_YN "
			+ "	FROM GS_INVENTORY_MST A																											 "
			+ "	JOIN GS_PRODUCT_INF D ON A.INVEN_S_CLS_NO = D.PRODUCT_NO																         "
			+ "	LEFT JOIN (SELECT A.DEL_YN , A.USE_YN , A.CART_ITEM_NO, A.CART_USR_ID															 "
			+ "			FROM GS_CART_INF A																									     "
			+ "			JOIN GS_ACCOUNT_INF B ON A.CART_USR_ID = B.USR_ID AND USR_ID = :usrId													 "
			+ "			WHERE A.USE_YN = 'Y' AND A.DEL_YN = 'N' ) F ON F.CART_ITEM_NO = A.INVEN_S_CLS_NO	 									 "
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
			+ "			WHERE A.USE_YN = 'Y' AND A.DEL_YN = 'N' ) F ON F.CART_ITEM_NO = A.INVEN_S_CLS_NO	 									 "
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
			+ "			WHERE A.USE_YN = 'Y' AND A.DEL_YN = 'N' ) F ON F.CART_ITEM_NO = A.INVEN_S_CLS_NO	 									 "
			+ "	WHERE INVEN_M_CLS_NO = :itemNo 																									       "
			+ "	AND CRG_DATE <= :cur 																												   "
			+ "	AND END_DATE = :endDate 																										       "
																															,nativeQuery=true)
	List<Object[]> findAllChemistry(@Param("usrId")String usrId, @Param("itemNo")String itemNo, @Param("cur")String cur, @Param("endDate")String endDate, Pageable pageable);

}
