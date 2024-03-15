package com.gongsik.gsr.api.mypage.usrPoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.usrPoint.entity.UsrPointEntity;

@Repository
public interface UsrPointRepository extends JpaRepository<UsrPointEntity, Long>{

	UsrPointEntity findByPointUsrId(String usrId);
	
	@Query(value= "								  "
			+ "        SELECT MAX(POINT_SEQ)	  "
			+ "        FROM GS_POINT_INF 		  "  ,nativeQuery=true)
	long find();



}
