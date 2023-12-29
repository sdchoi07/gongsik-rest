package com.gongsik.gsr.api.account.join.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data	
@Table(name = "GS_INTERNATIONAL_PH_NM")
public class InternationalPhoneNumberEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COUNTRY_ID")
	private long countryId;
	
	@Column(name = "COUNTRY_KO_NM", nullable = false)
	private String countryKoNm;
	
	@Column(name = "COUNTRY_EN_NM", nullable = false)
    private String countryEnNm;

	@Column(name = "COUNTRY_PH", nullable = false)
    private String countryPh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CRT_DT")
	private LocalDateTime crtDt;
   	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRE_DT")
	private LocalDateTime expireDt;
	
	@Column(name = "USE_YN")
	private String useYn;
	
	@Column(name = "DEL_YN")
	private String delYn;


}
