package com.gongsik.gsr.api.mypage.order.entity;

import com.gongsik.gsr.api.common.entity.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@IdClass(OrderMultiKey.class)	
@Table(name = "GS_ORDER_INF")
public class OrderEntity {
	
	@Id
	@Column(name = "USR_ID", nullable = false)
	private String usrId;
	
	@Id
	@Column(name = "USR_NM")
	private String usrNm;
	
	@Id
	@Column(name = "ORDER_NO")
	private String orderNo;
	
	@Id
	@Column(name = "ORDER_SEQ")
	private int orderSeq;
	
	@Column(name = "ITEM_IMG")
	private String itemImg;
	
	@Column(name = "ITEM_NM")
	private String itemNm;
	
	@Column(name = "ITEM_NO")
    private String itemNo;
	
	@Column(name = "ITEM_CNT")
	private Integer itemCnt;
	
	@Column(name = "ORDER_ST")
	private String orderSt;
	
	@Column(name = "ORDER_DT")
	private String orderDt;

	@Column(name = "ARRV_DT")
	private String arrvDt;
	
	@Column(name = "CANCEL_DT")
	private String cancelDt;
	
	@Column(name = "ORDER_ADDR")
	private String orderAddr;

	@Embedded
    private CommonEntity comonEntity;


}
