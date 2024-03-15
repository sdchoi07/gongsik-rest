package com.gongsik.gsr.api.payment.dto;


import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaymentDto {
	private long paymentSeq;
	private String paymentOrderNo;
	private String paymentUsrNm;
	private String paymentUsrId;
	private String paymentSt;
    private String paymentType;
	private int paymentAmount;
	private String paymentCardNo;
	private String paymentCardType;
	private String paymentBankNo;
	private String paymentBankType;
	private String paymentFinDt;
	private String paymentCancelDt;
	private String paymentReason;
	
}
