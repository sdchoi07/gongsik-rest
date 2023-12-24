package com.gongsik.gsr.api.account.join.entity;

import org.hibernate.annotations.Comment;

import com.gongsik.gsr.api.common.entity.CommonEntity;
import com.gongsik.gsr.api.main.menu.entity.MainMenuEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	
	@Column(name = "CRT_DT")
	private String crtDt;
   	
	@Column(name = "EXPIRE_DT")
	private String expireDt;


}
