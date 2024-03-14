package com.gongsik.gsr.api.common;

public class PaymentException extends Exception {
	
	static final long serialVersionUID = 1L;
	
    // 1. 기본생성자를 활용한 예외처리
	public PaymentException() {         
		super();
	}
	
	
	// 2. String 에러메시지 파라미터를 받는 예외처리 방법
	public PaymentException(String errMsg) {
		super(errMsg);                        // Super를 통해 부모클래스인 Exception을 활용
	}
}