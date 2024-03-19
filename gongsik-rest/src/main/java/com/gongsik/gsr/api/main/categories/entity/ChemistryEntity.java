package com.gongsik.gsr.api.main.categories.entity;

import com.gongsik.gsr.api.common.entity.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "GS_CHEMISTRY_INF")
public class ChemistryEntity {

	@Column(name = "CHEMISTRY_NM")
    private String chemistryNm;
	
	@Id
	@Column(name = "CHEMISTRY_NO")
    private String chemistryNo;

    @Column(name = "CHEMISTRY_TEXT")
    private String chemistryText;

    @Column(name = "CHEMISTRY_URL")
    private String chemistryUrl;
    
    @Column(name = "CHEMISTRY_SALES_CNT")
    private int chemistrySalesCnt;
 
    @Column(name = "CHEMISTRY_PRICE")
    private int chemistryPrice;
    
    @Embedded
    private CommonEntity comonEntity;
}
