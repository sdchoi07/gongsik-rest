package com.gongsik.gsr.api.payment.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InvenDto {
	private String itemNm;
	private String itemNo;
	private int invenCnt;
	private int itemPrice;
	private String itemUrl;
}
