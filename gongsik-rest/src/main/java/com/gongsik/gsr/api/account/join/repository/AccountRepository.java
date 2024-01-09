package com.gongsik.gsr.api.account.join.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String>{

	Optional<AccountEntity> findByUsrId(String usrId);

	long countByUsrId(String usrId);


}

