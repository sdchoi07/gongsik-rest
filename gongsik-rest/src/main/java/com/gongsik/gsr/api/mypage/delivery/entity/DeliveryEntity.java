package com.gongsik.gsr.api.mypage.delivery.entity;

import com.gongsik.gsr.api.common.entity.CommonEntity;
import com.gongsik.gsr.api.mypage.order.entity.OrderEntity;
import com.gongsik.gsr.api.mypage.order.entity.OrderMultiKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(DeliveryMultikey.class)	
@Table(name = "GS_DELV_AREA_INF")
public class DeliveryEntity {
	
	@Id
	@Column(name = "DELV_AREA_SEQ", nullable = false)
	private long delvAreaSeq;
	
	@Id
	@Column(name = "DELV_USR_ID", nullable = false)
	private String delvUsrId;
	
	@Id
	@Column(name = "DELV_USR_NM")
	private String delvUsrNm;
	
	@Column(name = "DELV_AREA_ADDR")
	private String delvAreaAddr;
	
	@Column(name = "DELV_AREA_NM")
	private String delvAreaNm;

	@Column(name = "DELV_USE_YN")
    private String delvUseYn;
	
	@Column(name = "DELV_AREA_NO")
	private String delvAreaNo;

	@Column(name = "DELV_PH_NO")
	private String delvPhNo;
	
	@Column(name = "USE_YN")
	private String useYn;
	
	@Column(name = "DEL_YN")
	private String delYn;

	@Embedded
    private CommonEntity comonEntity;
}
