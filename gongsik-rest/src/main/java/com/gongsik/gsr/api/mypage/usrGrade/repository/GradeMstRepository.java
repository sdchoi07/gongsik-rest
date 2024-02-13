package com.gongsik.gsr.api.mypage.usrGrade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.usrGrade.entity.GradeMstEntity;
@Repository
public interface GradeMstRepository extends JpaRepository<GradeMstEntity, String>{

}
