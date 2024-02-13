package com.gongsik.gsr.api.mypage.usrGrade.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.usrGrade.entity.UsrGradeEntity;

@Repository
public interface UsrGradeRepository  extends JpaRepository<UsrGradeEntity, Long>{

	Optional<UsrGradeEntity> findByGradeUsrId(String usrId);

}
