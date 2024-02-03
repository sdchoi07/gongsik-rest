package com.gongsik.gsr.api.mypage.profile.dto;

import java.time.LocalDateTime;

import com.gongsik.gsr.api.account.join.dto.JoinDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "profileDto")
public class ProfileDto {
	
	/* 유저 정보 */
	private String usrId;
	private String usrNm;
	private String usrNo;
	private String usrSex;
	private String usrPwd;
	private String usrPhone;
	private String usrAddr;
	private String usrDelvArea;
	private String usrGrade;
	private String usrPurchaseCnt;
	private String usrStatus;
	private String usrRole;
	private LocalDateTime usrLogInDt;
	private LocalDateTime usrLogOutDt;
	private String logTp;
}
