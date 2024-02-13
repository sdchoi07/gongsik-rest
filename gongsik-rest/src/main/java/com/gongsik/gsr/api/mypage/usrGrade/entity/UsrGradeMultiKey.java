package com.gongsik.gsr.api.mypage.usrGrade.entity;

import java.io.Serializable;

import com.gongsik.gsr.api.mypage.profile.entity.ProfileMultiKey;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor		//전체생성자
@NoArgsConstructor		//기본 생성자
@EqualsAndHashCode		//equals, hashCode
@Data
public class UsrGradeMultiKey implements Serializable {
	@Column(name = "GRADE_SEQ")
	private long gradeSeq;
	@Column(name = "GRADE_USR_ID")
	private String gradeUsrId;
}
