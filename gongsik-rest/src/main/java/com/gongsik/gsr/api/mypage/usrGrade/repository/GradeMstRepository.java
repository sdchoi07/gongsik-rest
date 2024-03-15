package com.gongsik.gsr.api.mypage.usrGrade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.mypage.usrGrade.entity.GradeMstEntity;

@Repository
public interface GradeMstRepository extends JpaRepository<GradeMstEntity, String> {

	@Query(value=
		        "	 SELECT MAX(GRADE_LEVEL) 											"
		      + "	   FROM GS_GRADE_MST											"
		      + "	  WHERE GRADE_POINT <= :sumAmount 								"
																															,nativeQuery=true)
	String findByGradePoint(@Param("sumAmount")int sumAmount);

}
