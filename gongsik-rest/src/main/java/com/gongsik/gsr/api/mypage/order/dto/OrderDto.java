package com.gongsik.gsr.api.mypage.order.dto;

import com.gongsik.gsr.api.common.dto.CommonDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "orderDto")
public class OrderDto {
	
	/* 주문 내역 */
	private String itemNm;
	private Integer itemCnt;
	private String orderDt;
	private String orderSt;
	private String usrId;
	private String usrNm;
	private Long orderSeq;
	private String itemNo;
	private String arrvDt;
	private String cancelDt;
	private String itemImg;
	private String orderStNm;
	private String orderNo;
	private String orderAddr;
	private String itemPrice;
}
