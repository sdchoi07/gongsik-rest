package com.gongsik.gsr.api.admin.dto;

import com.gongsik.gsr.api.chat.dto.ChatDto;
import com.gongsik.gsr.api.common.dto.CommonDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "ItemListDto")
public class ItemListDto{
	
	/* 아이템 */
	private String invenLClsNm;
	private String invenMClsNm;
	private String invenSClsNm;
	private String invenLClsNo;
	private String invenMClsNo;
    private String invenSClsNo;
	private int itemPrice;
	private int itemSalesCnt;
	
}