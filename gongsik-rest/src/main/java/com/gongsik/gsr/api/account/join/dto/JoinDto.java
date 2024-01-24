package com.gongsik.gsr.api.account.join.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "joinDto")
public class JoinDto {
	/* 국제 번호 */
	private String countryKoNm;
	private String countryEnNm;
	private String countryPh;
	private String countryFullNm;
	private LocalDateTime crtDt;
	private LocalDateTime expiredt;
	private String useYn;
	private String delYn;
	
	/* 인증번호 */
	private long smsSeq;
	private String authId;
	private String usrId;
	private String usrPhNo;
	private String authNo;
	private int reReqAuthCnt;
	private String authYn;
	private String authType;
	private LocalDateTime reqDt;
	private LocalDateTime confDt;
	private long cnt;
	
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
