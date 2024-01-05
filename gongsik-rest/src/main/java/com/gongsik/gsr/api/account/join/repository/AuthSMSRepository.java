package com.gongsik.gsr.api.account.join.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.account.join.entity.AuthSMSEntity;

@Repository
public interface AuthSMSRepository extends JpaRepository<AuthSMSEntity, String> {


	Optional<AuthSMSEntity> findByUsrPhNo(String usrPhNo);
	


}
