package com.gongsik.gsr.api.mypage.uscart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.uscart.dto.CartDto;
import com.gongsik.gsr.api.mypage.uscart.entity.CartEntity;

@Repository
public interface CartRepository  extends JpaRepository<CartEntity, Long>{
	@Query(value=
			  "	 SELECT 																																	"
			+ "        A.CART_NO,																															"
			+ "        A.CART_USR_ID,																														"
			+ "        A.CART_ITEM_CNT,																														"
			+ "        A.CART_ITEM_NM,																													    "
			+ "        A.CART_ITEM_NO,																													    "
			+ "        A.CART_ST,																													        "
			+ "         CASE																															    "
			+ "	        WHEN B.PRODUCT_URL IS NOT NULL THEN B.PRODUCT_URL																		            "
			+ "	        WHEN C.SEED_URL IS NOT NULL THEN C.SEED_URL																							"
			+ "	        WHEN D.CHEMISTRY_URL IS NOT NULL THEN D.CHEMISTRY_URL																			    "
			+ "        ELSE ''																															    "
			+ "	    END AS CART_URL,																															    "
			+ "         CASE																																"
			+ "	        WHEN B.PRODUCT_PRICE IS NOT NULL THEN B.PRODUCT_PRICE																				"
			+ "	        WHEN C.SEED_PRICE IS NOT NULL THEN C.SEED_PRICE																						"
			+ "	        WHEN D.CHEMISTRY_URL IS NOT NULL THEN D.CHEMISTRY_PRICE																		        "
			+ "        ELSE 0																															    "
			+ "	    END AS CART_PRICE,																															"
			+ "        A.DEL_YN,																															"
			+ "        A.USE_YN 																															"
			+ "    FROM																																	    "
			+ "        GS_CART_INF A																														"
			+ "    LEFT JOIN (SELECT PRODUCT_URL, PRODUCT_PRICE, PRODUCT_NO FROM GS_PRODUCT_INF) B ON A.CART_ITEM_NO = B.PRODUCT_NO  						"
			+ "    LEFT JOIN (SELECT SEED_URL, SEED_PRICE, SEED_NO FROM GS_SEED_INF) C ON A.CART_ITEM_NO = C.SEED_NO  									    "
			+ "    LEFT JOIN (SELECT CHEMISTRY_URL, CHEMISTRY_PRICE, CHEMISTRY_NO FROM GS_CHEMISTRY_INF) D ON A.CART_ITEM_NO = D.CHEMISTRY_NO  				"
			+ "    WHERE																																	"
			+ "        A.CART_USR_ID= :usrId 																										"
			+ "        AND A.CART_ST= :cartSt 																											        "
			+ "        AND A.DEL_YN= :delYn																													    "
			+ "		   AND A.USE_YN= :useYn 																													    "
			+ "    ORDER BY																																	"
			+ "        A.CART_NO DESC"
																															,nativeQuery=true)
	List<Object[]> findByCartUsrIdAndCartStAndDelYnAndUseYnOrderByCartNoDesc(@Param("usrId")String usrId, @Param("cartSt")String cartSt, @Param("delYn")String delYn, @Param("useYn")String useYn);

	Optional<CartEntity> findByCartUsrIdAndCartNo(String usrId, long cartNo);
	
	@Query(value=
		      "	SELECT IFNULL(MAX(CART_NO),0) AS CART_NO "
			+ "	FROM GS_CART_INF A	"
																															,nativeQuery=true)
	int findOne();

	Optional<CartEntity> findByCartItemNoAndCartStAndCartUsrIdAndUseYnAndDelYn(String cartItemNo, String cartSt,
			String usrId, String string, String string2);

}
