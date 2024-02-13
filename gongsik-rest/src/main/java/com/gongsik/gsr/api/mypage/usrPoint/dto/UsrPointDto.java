package com.gongsik.gsr.api.mypage.usrPoint.dto;



import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
@Schema(description = "UsrPointDto")
public class UsrPointDto {
	
	/* 유저 포인트 */
	private long pointSeq;
	private String pointUsrId;
	private String pointPt;
	private String pointSt;
	private String pointStNm;
	private String pointDt;
    private LocalDateTime pointCrtDt;
    private LocalDateTime pointUsedDt;
	private LocalDateTime pointExpireDt;
	
	@QueryProjection
	  public UsrPointDto(String pointPt, String pointSt, String pointStNm, String pointDt) {
	    this.pointPt = pointPt;
	    this.pointSt = pointSt;
	    this.pointStNm = pointStNm;
	    this.pointDt = pointDt;
	  }
}
