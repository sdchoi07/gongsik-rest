package com.gongsik.gsr.api.mypage.delivery.dto;

import com.gongsik.gsr.api.mypage.order.dto.OrderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
@Schema(description = "DeliveryDto")
public class DeliveryDto {
	
	private long delvAreaSeq;
	private String delvUsrId;
	private String delvUsrNm;
	private String delvAreaAddr;
	private String delvAreaNm;
    private String delvUseYn;
	private String delvAreaNo;
	private String delvUseYnNm;
	private String delvPhNo;
	private String delvAreaDetail;
	private String useYn;
	private String delYn;
	
	private String usrPhone;
}
