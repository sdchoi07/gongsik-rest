package com.gongsik.gsr.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class resultDto {
	private String errCode;
	private String errMsg;
	private List<?> list;
}
