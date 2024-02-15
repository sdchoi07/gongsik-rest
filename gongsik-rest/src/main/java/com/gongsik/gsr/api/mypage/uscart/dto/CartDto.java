package com.gongsik.gsr.api.mypage.uscart.dto;

import com.gongsik.gsr.api.common.dto.CommonDto;
import com.gongsik.gsr.api.mypage.usrGrade.dto.GradeMstDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@Schema(description = "CartDto")
public class CartDto extends CommonDto{
	
	private long cartNo;
	private String cartUsrId;
	private String cartSt;
	private String cartItemNo;
	private String cartItemNm;
	private int cartItemCnt;
	private String delYn;
}
