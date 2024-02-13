package com.gongsik.gsr.api.mypage.usrGrade.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
@Schema(description = "UsrGradeDto")
public class UsrGradeDto {
	private long gradeSeq;
	private String gradeUsrId;
	private String gradeUsrNm;
	private String gradeLevel;
	private String finDt;

}
