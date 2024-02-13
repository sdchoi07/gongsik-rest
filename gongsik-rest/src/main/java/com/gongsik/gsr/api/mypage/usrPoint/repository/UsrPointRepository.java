package com.gongsik.gsr.api.mypage.usrPoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.usrPoint.entity.UsrPointEntity;

@Repository
public interface UsrPointRepository extends JpaRepository<UsrPointEntity, Long>{

}
