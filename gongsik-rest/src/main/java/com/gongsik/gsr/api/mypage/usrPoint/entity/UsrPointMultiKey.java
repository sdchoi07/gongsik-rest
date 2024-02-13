package com.gongsik.gsr.api.mypage.usrPoint.entity;

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
public class UsrPointMultiKey implements Serializable{
	
    @Column(name = "POINT_SEQ")		//3번
    private long pointSeq;
	@Column(name = "POINT_USR_ID")
	private String pointUsrId;

}
