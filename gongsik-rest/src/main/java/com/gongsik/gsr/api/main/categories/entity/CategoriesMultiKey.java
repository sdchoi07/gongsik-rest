package com.gongsik.gsr.api.main.categories.entity;

import java.io.Serializable;

import com.gongsik.gsr.api.mypage.usrGrade.entity.UsrGradeMultiKey;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor		//전체생성자
@NoArgsConstructor		//기본 생성자
@EqualsAndHashCode		//equals, hashCode
@Data
public class CategoriesMultiKey implements Serializable {
	
	@Column(name = "INVEN_L_CLS_NO")
    private String invenLClsNo;
	@Column(name = "INVEN_M_CLS_NO")
    private String invenMClsNo;
    @Column(name = "INVEN_S_CLS_NO")
    private String invenSClsNo;
}
