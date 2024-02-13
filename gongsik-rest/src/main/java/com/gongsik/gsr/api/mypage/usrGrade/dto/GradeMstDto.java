package com.gongsik.gsr.api.mypage.usrGrade.dto;

import java.time.LocalDateTime;

import com.gongsik.gsr.api.common.dto.CommonDto;
import com.gongsik.gsr.api.mypage.profile.dto.ProfileDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "GradeMstDto")
public class GradeMstDto extends CommonDto{
	
	private String gradeLevel;
	private String gradeDesc;
	private int gradePoint;
}
