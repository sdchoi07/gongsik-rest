package com.gongsik.gsr.api.account.join.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String>{

	Optional<AccountEntity> findByUsrId(String AccountMultiKey);

	long countByUsrId(String usrId);

	AccountEntity findByUsrIdAndProviderId(String email, String providerId);

	Optional<AccountEntity> findByUsrIdAndProviderIdAndLogTp(String email, String providerId, String logTp);
	
	Optional<AccountEntity> findByUsrPhone(String phoneNumber);

	Optional<AccountEntity> findByUsrIdAndLogTpAndUsrNm(String usrId, String logTp, String usrNm);

	Optional<List<AccountEntity>> findByChatYnAndUsrIdNot(String string, String usrId);

	Optional<AccountEntity> findByUsrNm(String chatRoomReciver);
	
	@Query(value=
		      "	    SELECT																"
		      + "	 		A.USR_ID													"
		      + "	       ,A.USR_NM													"
		      + "	       ,A.USR_PHONE													"
		      + "		   ,B.DELV_AREA_ADDR											"
		      + "          ,B.DELV_AREA_NO 												"
		      + "     FROM																"
		      + "	     ( 															    "
		      + "			(															"
		      + "	          SELECT													"
		      + "					  USR_ID											"
		      + "					 ,USR_NM											"
		      + "					 ,USR_PHONE											"
		      + "			   FROM	 GS_ACCOUNT_INF A								    "
		      + "			  WHERE	 A.USR_ID = :usrId									"
		      + "			)A															"
		      + "       LEFT JOIN													    "
	          + "		(																"
		      + "			 SELECT														"
		      + "					DELV_USR_ID 										"
		      + "		           ,DELV_AREA_ADDR										"
		      + "				   ,DELV_AREA_NO										"
		      + "			  FROM GS_DELV_AREA_INF										"
		      + "			 WHERE DELV_USR_ID = :usrId									"
		      + "			   AND DELV_USE_YN = 'Y'									"
		      + "		       AND USE_YN = 'Y'											"
		      + "			   AND DEL_YN = 'N'											"
		      + "			)B															"
		      + "			ON A.USR_ID = B.DELV_USR_ID)								"
																															,nativeQuery=true)
	Optional<Object[]> findByUsrId2(@Param("usrId")String usrId);

	Optional<AccountEntity> findByUsrIdAndLogTp(String usrId, String logTp);

}

