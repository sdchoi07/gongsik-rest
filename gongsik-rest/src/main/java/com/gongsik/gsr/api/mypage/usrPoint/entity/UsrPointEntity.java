package com.gongsik.gsr.api.mypage.usrPoint.entity;

import com.gongsik.gsr.api.account.join.entity.AccountMultiKey;
import com.gongsik.gsr.api.mypage.profile.entity.ProfileEntity;

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
@IdClass(UsrPointMultiKey.class)	
@Table(name = "GS_POINT_INF")
public class UsrPointEntity {
	
	@Id
	@Column(name = "POINT_SEQ", nullable = false)
	private long pointSeq;
	
	@Id
	@Column(name = "POINT_USR_ID")
	private String pointUsrId;
	
	@Column(name = "POINT_PT")
	private String pointPt;
	
	@Column(name = "POINT_ST")
	private String pointSt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POINT_CRT_DT")
    private String pointCrtDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POINT_USED_DT")
    private String pointUsedDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "POINT_EXPIRE_DT")
	private String pointExpireDt;
	
	
}
