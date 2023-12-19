package com.gongsik.gsr.api.main.dto;

import com.gongsik.gsr.api.common.dto.CommonDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "MenuList")
public class MainMenuDto extends CommonDto{
	
	private String menuNm;
	private int menuNO;
	private String menuGroupNo;
	private int menuOrderNo;
	private int menuLevelNo;
	private String menuUrl;
	
	
}
