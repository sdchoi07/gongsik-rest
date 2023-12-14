package com.gongsik.gsr.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonDto {
	private String regId;
	private String regIp;
	private String regDt;
	private String chgId;
	private String chgIp;
	private String chgDt;
}
