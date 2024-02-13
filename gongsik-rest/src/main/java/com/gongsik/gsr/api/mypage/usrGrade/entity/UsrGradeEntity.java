package com.gongsik.gsr.api.mypage.usrGrade.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data	
@IdClass(UsrGradeMultiKey.class)	
@Table(name = "GS_GRADE_INF")
public class UsrGradeEntity {
	@Id
	@Column(name = "GRADE_SEQ")
	private long gradeSeq;
	
	@Id
	@Column(name = "GRADE_USR_ID")
	private String gradeUsrId;
	
	@Column(name = "GRADE_USR_NM")
	private String gradeUsrNm;
	
	@Column(name = "GRADE_LEVEL")
	private String gradeLevel;
	
	@Column(name = "FIN_DT")
	private LocalDateTime finDt;

}
