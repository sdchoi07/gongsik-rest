package com.gongsik.gsr.api.account.join.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "countryPh")
public class JoinDto {
	private String countryKoNm;
	private String countryEnNm;
	private String countryPh;
	private String countryFullNm;
}
