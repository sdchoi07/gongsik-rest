package com.gongsik.gsr.api.mypage.profile.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor		//전체생성자
@NoArgsConstructor		//기본 생성자
@EqualsAndHashCode		//equals, hashCode
@Data
public class ProfileMultiKey implements Serializable{
    @Column(name = "USR_ID")		//3번
    private String usrId;
	@Column(name = "LOG_TP")
	private String logTp;

}
