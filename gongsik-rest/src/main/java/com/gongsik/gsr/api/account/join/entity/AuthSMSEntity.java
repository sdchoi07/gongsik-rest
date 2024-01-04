package com.gongsik.gsr.api.account.join.entity;

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
@Table(name = "GS_AUTH_SMS_INF")
public class AuthSMSEntity {
	
	@Id
	@Column(name = "USR_ID", nullable = false)
	private String usrId;

	@Column(name = "AUTH_ID")
	private String authId;
	
	@Column(name = "COUNTRY_PH")
	private String countryPh;
	
	@Column(name = "USR_PH_NO", nullable = false)
    private String usrPhNo;

	@Column(name = "AUTH_NO", nullable = false)
    private String authNo;
	
	@Column(name = "RE_REQ_AUTH_CNT")
	private int reReqAuthCnt;
	
	@Column(name = "AUTH_YN")
	private String authYn;
	
	@Column(name = "AUTH_TYPE")
	private String authType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQ_DT")
	private String reqDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONF_DT")
	private String confDt;
	
}
