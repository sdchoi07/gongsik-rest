package com.gongsik.gsr.api.main.categories.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.admin.dto.ItemListDto;
import com.gongsik.gsr.api.main.categories.entity.CategoriesEntity;
import com.gongsik.gsr.api.payment.dto.InvenDto;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, String>{

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
		      "			SELECT  new com.gongsik.gsr.api.payment.dto.InvenDto(                                                "
		      + "				 A.invenSClsNm  AS itemNm																"
		      + "				,A.invenSClsNo  AS itemNo																"
		      + "				,A.invenCnt 	   AS invenCnt																"
		      + "				,CASE WHEN B.chemistryPrice  IS NOT NULL THEN B.chemistryPrice								"
		      + "		      		WHEN C.productPrice IS NOT NULL THEN C.productPrice									"
		      + "		      		WHEN D.seedPrice IS NOT NULL THEN D.seedPrice ELSE 0 END AS itemPrice				"
		      + "               ,CASE WHEN B.chemistryUrl  IS NOT NULL THEN B.chemistryUrl 								"
		      + "      		       WHEN C.productUrl IS NOT NULL THEN C.productUrl										"
		      + "      		       WHEN D.seedUrl IS NOT NULL THEN D.seedUrl ELSE 0 END AS itemUrl)						"
		      + "		FROM CategoriesEntity A																				"
		      + "		LEFT JOIN ChemistryEntity B ON A.invenSClsNo  = B.chemistryNo 									"
		      + "		LEFT JOIN ProductEntity C ON A.invenSClsNo = C.productNo 										"
		      + "		LEFT JOIN SeedEntity D ON A.invenSClsNo = D.seedNo 											"
		      + "		WHERE A.invenSClsNo = :itemNo 																	"			,nativeQuery=false)
	InvenDto findByInvneSClsNo(@Param("itemNo")String itemNo);
	
	@Modifying
	@Query(value=
		      "			UPDATE  GS_INVENTORY_MST 			"
		      + "		   SET INVEN_CNT  = :invenCnt		"
		      + "        WHERE INVEN_S_CLS_NO = :itemNo  		"
																															,nativeQuery=true)
	void updateItemCnt(@Param("itemNo")String itemNo, @Param("invenCnt")int invenCnt);
	
	@Query(value=
		      "			SELECT 			new com.gongsik.gsr.api.admin.dto.ItemListDto(																				    "
		      + "		 	 A.invenLClsNm as invenLClsNm																			"
		      + "			,A.invenMClsNm as invenMClsNm 																				"
		      + "			,A.invenSClsNm as invenSClsNm 																				"
		      + "			,A.invenLClsNo as invenLClsNo																				"
		      + "			,A.invenMClsNo as invenMClsNo																				"
		      + "			,A.invenSClsNo as invenSClsNo																				"
		      + "			,CASE WHEN B.productPrice IS NOT NULL THEN B.productPrice										"
		      + "        		WHEN C.chemistryPrice IS NOT NULL THEN C.chemistryPrice									"
		      + "	        	WHEN D.seedPrice IS NOT NULL THEN D.seedPrice ELSE 0 END									"
		      + "				AS itemPrice 																				"
		      + "			,CASE WHEN B.productSalesCnt  IS NOT NULL THEN B.productSalesCnt						 	"
		      + "        		WHEN C.chemistrySalesCnt  IS NOT NULL THEN C.chemistrySalesCnt							"
		      + "	        	WHEN D.seedSalesCnt  IS NOT NULL THEN D.seedSalesCnt ELSE 0 END							"
		      + "				AS itemSalesCnt 							)												"
		      + "	FROM CategoriesEntity A																					"
		      + "	LEFT JOIN ProductEntity B ON A.invenSClsNo = B.productNo 											"
		      + "	LEFT JOIN ChemistryEntity C ON A.invenSClsNo = C.chemistryNo										"
		      + "	LEFT JOIN SeedEntity D ON A.invenSClsNo = D.seedNo  												"
		      + "	ORDER BY invenSClsNo												"			,nativeQuery=false)
	List<ItemListDto> findByEndDate(@Param("endDate")String endDate);
}
