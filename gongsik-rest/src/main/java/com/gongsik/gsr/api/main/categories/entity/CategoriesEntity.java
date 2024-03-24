package com.gongsik.gsr.api.main.categories.entity;

import com.gongsik.gsr.api.common.entity.CommonEntity;

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
@Table(name = "GS_INVENTORY_MST")
@IdClass(CategoriesMultiKey.class)	
public class CategoriesEntity {
	
	@Column(name = "INVEN_L_CLS_NM")
    private String invenLClsNm;
	
	@Column(name = "INVEN_M_CLS_NM")
    private String invenMClsNm;

	@Column(name = "INVEN_S_CLS_NM")
    private String invenSClsNm;
	
	@Id
    @Column(name = "INVEN_L_CLS_NO")
    private String invenLClsNo;
	
	@Id
    @Column(name = "INVEN_M_CLS_NO")
    private String invenMClsNo;
	
	@Id
    @Column(name = "INVEN_S_CLS_NO")
    private String invenSClsNo;
    
    @Column(name = "INVEN_CNT")
    private int invenCnt;
    
    @Column(name = "CRG_DATE")
    private String crgDate;
    
    @Column(name = "END_DATE")
    private String endDate;
   	
    @Embedded
    private CommonEntity comonEntity;

    

}
