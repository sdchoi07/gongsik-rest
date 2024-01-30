package com.gongsik.gsr.api.account.join.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.entity.AccountMultiKey;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String>{

	Optional<AccountEntity> findByUsrId(String AccountMultiKey);

	long countByUsrId(String usrId);

	AccountEntity findByUsrIdAndProviderId(String email, String providerId);

	Optional<AccountEntity> findByUsrIdAndProviderIdAndLogTp(String email, String providerId, String logTp);
	
	Optional<AccountEntity> findByUsrPhone(String phoneNumber);

	Optional<AccountEntity> findByUsrIdAndLogTpAndUsrNm(String usrId, String logTp, String usrNm);

}

