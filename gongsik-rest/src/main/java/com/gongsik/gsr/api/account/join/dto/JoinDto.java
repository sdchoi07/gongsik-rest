package com.gongsik.gsr.api.account.join.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "countryPh")
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
	
}
