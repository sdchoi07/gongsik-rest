package com.gongsik.gsr.api.account.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String>{

	long countByUsrId(String usrId);


}

