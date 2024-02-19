package com.gongsik.gsr.api.mypage.uscart.entity;

import java.time.LocalDateTime;

import com.gongsik.gsr.api.mypage.usrGrade.entity.GradeMstEntity;
import com.gongsik.gsr.api.mypage.usrGrade.entity.UsrGradeMultiKey;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@IdClass(CartMultiKey.class)	
@Table(name = "GS_CART_INF")
public class CartEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CART_NO")
	private long cartNo;
	
	@Id
	@Column(name ="CART_USR_ID")
	private String cartUsrId;
	
	@Column(name ="CART_ST")
	private String cartSt;
	
	@Column(name = "CART_ITEM_NO")
	private String cartItemNo;

	@Column(name = "CART_ITEM_NM")
	private String cartItemNm;

	@Column(name = "CART_ITEM_CNT")
	private int cartItemCnt;
	
	@Column(name = "DEL_YN")
	private String delYn;
	
	@Column(name = "USE_YN")
	private String useYn;

}
