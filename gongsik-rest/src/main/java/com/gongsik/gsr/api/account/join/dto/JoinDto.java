package com.gongsik.gsr.api.account.join.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
	private LocalDateTime crtDt;
	private LocalDateTime expiredt;
	private String useYn;
	private String delYn;
}
