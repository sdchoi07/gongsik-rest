package com.gongsik.gsr.api.account.join.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.join.entity.AuthSMSEntity;
import com.gongsik.gsr.api.account.join.entity.AuthSMSHistEntity;
import com.gongsik.gsr.api.account.join.entity.InternationalPhoneNumberEntity;

@Repository
public interface InternationalPhoneNumberRepository extends JpaRepository<InternationalPhoneNumberEntity, Long> {
	
	//국제번호 list 조회
	@Query(value =  "       SELECT new com.gongsik.gsr.api.account.join.dto.JoinDto																						"
				  + "          	(																																		"
				  + "			  CONCAT(a.countryKoNm, '(', a.countryEnNm, ')' , ' +', a.countryPh) AS countryFullNm    												"
				  + "			  ,a.countryPh AS countryPh   		 																									"
				  + "			)	                       																											 	"
				  + "		 FROM InternationalPhoneNumberEntity a																									    "
				  + "        WHERE DATE_FORMAT(a.crtDt,'%Y-&m-&D') <= DATE_FORMAT( CURRENT_TIMESTAMP, '%Y-%m-%d') 													    "
				  + "          AND DATE_FORMAT( a.expireDt, '%Y-%m-%d') = DATE_FORMAT( '9999-12-31', '%Y-%m-%d')														"
				  + "          AND a.useYn = 'Y' 																														"
				  + "          AND a.delYn = 'N' 																														")
	List<JoinDto> findAllOnlyCountryPhNm();

	
	
}


