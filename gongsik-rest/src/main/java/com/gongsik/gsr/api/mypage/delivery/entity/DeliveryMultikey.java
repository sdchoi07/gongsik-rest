package com.gongsik.gsr.api.mypage.delivery.entity;

import java.io.Serializable;

import com.gongsik.gsr.api.mypage.order.entity.OrderMultiKey;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor		//전체생성자
@NoArgsConstructor		//기본 생성자
@EqualsAndHashCode		//equals, hashCode
@Data
public class DeliveryMultikey implements Serializable{

	@Column(name = "DELV_AREA_SEQ")
	private long delvAreaSeq;
	
    @Column(name = "DELV_USR_NM")		
    private String delvUsrNm;
    
	@Column(name = "DELV_USR_ID")
	private String delvUsrId;
	
}
