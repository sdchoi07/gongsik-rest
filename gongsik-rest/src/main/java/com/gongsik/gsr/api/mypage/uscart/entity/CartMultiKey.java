package com.gongsik.gsr.api.mypage.uscart.entity;

import java.io.Serializable;

import com.gongsik.gsr.api.mypage.usrGrade.entity.UsrGradeMultiKey;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor		//전체생성자
@NoArgsConstructor		//기본 생성자
@EqualsAndHashCode		//equals, hashCode
@Data
public class CartMultiKey implements Serializable {
	@Column(name = "CART_NO")
	private long cartNo;
	@Column(name ="CART_USR_ID")
	private String cartUsrId;
	@Column(name ="CART_ST")
	private String cartSt;
}
