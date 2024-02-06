package com.gongsik.gsr.api.mypage.order.entity;

import java.io.Serializable;

import com.gongsik.gsr.api.mypage.profile.entity.ProfileMultiKey;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor		//전체생성자
@NoArgsConstructor		//기본 생성자
@EqualsAndHashCode		//equals, hashCode
@Data
public class OrderMultiKey implements Serializable{
	
	@Column(name = "ORDER_SEQ")
	private long orderSeq;
	
    @Column(name = "USR_ID")		
    private String usrId;
    
	@Column(name = "USR_NM")
	private String usrNm;
	
}
