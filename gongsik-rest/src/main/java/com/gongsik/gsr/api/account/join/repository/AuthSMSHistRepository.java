package com.gongsik.gsr.api.account.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.account.join.entity.AuthSMSHistEntity;

@Repository
public interface AuthSMSHistRepository extends JpaRepository<AuthSMSHistEntity, Long> {
	


}
