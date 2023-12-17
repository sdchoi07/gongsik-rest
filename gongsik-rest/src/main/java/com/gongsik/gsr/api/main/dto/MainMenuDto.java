package com.gongsik.gsr.api.main.dto;

import com.gongsik.gsr.api.CommonDto;
import com.gongsik.gsr.api.main.entity.MainMenuEntity;

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
	private String menuGroupNo;
	private int menuOrderNo;
	private String menuParentNo;
	private String menuUrl;
	
	
}
