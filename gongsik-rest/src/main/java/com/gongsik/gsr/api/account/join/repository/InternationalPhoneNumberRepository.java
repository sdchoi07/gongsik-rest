package com.gongsik.gsr.api.account.join.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.join.entity.InternationalPhoneNumberEntity;

@Repository
public interface InternationalPhoneNumberRepository extends JpaRepository<InternationalPhoneNumberEntity, Long> {
	
	//국제번호 list 조회
	@Query(value =  "       SELECT new com.gongsik.gsr.api.account.join.dto.JoinDto																						"
				  + "          	(																																		"
				  + "			  CONCAT(a.countryKoNm, '(', REGEXP_REPLACE(a.countryEnNm,' ', ''), ')' , ' +', REGEXP_REPLACE(a.countryPh,' ','')) AS countryFullNm    "
				  + "      		 ,a.countryPh AS countryPh																						"
				  + "           )                       																											 	"
				  + "		 FROM InternationalPhoneNumberEntity a")
	List<JoinDto> findAllOnlyCountryPhNm();
	
}


