package com.gongsik.gsr.api.mypage.usrGrade.entity;

import com.gongsik.gsr.api.account.join.entity.AccountMultiKey;
import com.gongsik.gsr.api.mypage.profile.entity.ProfileEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data	
@Table(name = "GS_GRADE_MST")
public class GradeMstEntity {

	@Id
	@Column(name = "GRADE_LEVEL", nullable = false)
	private String gradeLevel;
	
	@Column(name = "GRADE_DESC")
	private String gradeDesc;
	
	@Column(name = "GRADE_POINT")
	private int gradePoint;
	

}
