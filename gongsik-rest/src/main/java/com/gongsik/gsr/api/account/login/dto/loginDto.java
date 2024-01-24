package com.gongsik.gsr.api.account.login.dto;

import java.time.LocalDateTime;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
@Schema(description = "loginDto")
public class loginDto {
	/* 회원가입 */
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
}
