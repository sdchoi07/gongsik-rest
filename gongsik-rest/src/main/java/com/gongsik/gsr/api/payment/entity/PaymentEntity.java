package com.gongsik.gsr.api.payment.entity;

import com.gongsik.gsr.api.common.entity.CommonEntity;
import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryEntity;
import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryMultikey;

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
@Table(name = "GS_PAYMENT_INF")
public class PaymentEntity {
	
	@Id
	@Column(name = "PAYMENT_SEQ", nullable = false)
	private long paymentSeq;
	
	@Column(name = "PAYMENT_ORDER_NO", nullable = false)
	private String paymentOrderNo;
	
	@Column(name = "PAYMENT_USR_NM")
	private String paymentUsrNm;
	
	@Column(name = "PAYMENT_USR_ID")
	private String paymentUsrId;
	
	@Column(name = "PAYMENT_ST")
	private String paymentSt;

	@Column(name = "PAYMENT_TYPE")
    private String paymentType;
	
	@Column(name = "PAYMENT_AMOUNT")
	private int paymentAmount;

	@Column(name = "PAYMENT_CARD_NO")
	private String paymentCardNo;
	
	@Column(name = "PAYMENT_CARD_TYPE")
	private String paymentCardType;
	
	@Column(name = "PAYMENT_BANK_NO")
	private String paymentBankNo;
	
	@Column(name = "PAYMENT_BANK_TYPE")
	private String paymentBankType;
	
	@Column(name = "PAYMENT_FIN_DT")
	private String paymentFinDt;
	
	@Column(name = "PAYMENT_CANCEL_DT")
	private String paymentCancelDt;

	@Column(name = "PAYMENT_REASON")
	private String paymentReason;
	
	@Embedded
    private CommonEntity comonEntity;

}
