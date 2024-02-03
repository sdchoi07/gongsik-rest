package com.gongsik.gsr.api.mypage.profile.entity;

import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.entity.AccountMultiKey;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(AccountMultiKey.class)	
@Table(name = "GS_ACCOUNT_INF")
public class ProfileEntity {
	
	@Id
	@Column(name = "USR_ID", nullable = false)
	private String usrId;
	
	@Id
	@Column(name = "LOG_TP")
	private String logTp;
	
	@Column(name = "USR_NM")
	private String usrNm;
	
	@Column(name = "USR_NO")
	private String usrNo;

	@Column(name = "USR_SEX", nullable = false)
    private String usrSex;
	
	@Column(name = "USR_PWD")
	private String usrPwd;
	
	@Column(name = "USR_ADDR")
	private String usrAddr;
	
	@Column(name = "USR_PHONE")
	private String usrPhone;

	@Column(name = "USR_DELV_AREA")
	private String usrDelvArea;
	
	@Column(name = "USR_GRADE")
	private String usrGrade;

	@Column(name = "USR_PURCHASE_CNT")
	private String usrPurchaseCnt;

	@Column(name = "USR_STATUS")
	private String usrStatus;
	
	@Column(name = "USR_ROLE")
	private String usrRole;
	
	@Column(name = "COUNTRY_PH")
	private String countryPh;
	
	@Column(name = "PROVIDER")
	private String provider;
	
	@Column(name = "PROVIDER_ID")
	private String providerId;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "USR_LOG_IN_DT")
	private String usrLogInDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "USR_LOG_OUT_DT")
	private String usrLogOutDt;

}
