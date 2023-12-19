package com.gongsik.gsr.api.common.dto;

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
public class CommonDto {
	private String regId;
	private String regIp;
	private String regDt;
	private String chgId;
	private String chgIp;
	private String chgDt;
}

