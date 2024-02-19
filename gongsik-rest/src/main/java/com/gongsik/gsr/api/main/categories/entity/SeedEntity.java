package com.gongsik.gsr.api.main.categories.entity;

import com.gongsik.gsr.api.common.entity.CommonEntity;

import jakarta.persistence.Column;
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
@Table(name = "GS_SEED_INF")
public class SeedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEED_SEQ")
	private long seedSeq;
	
	@Column(name = "SEED_NM")
    private String seedNm;

	@Column(name = "SEED_NO")
    private String seedNo;

    @Column(name = "SEED_TEXT")
    private String seedText;

    @Column(name = "SEED_URL")
    private String seedUrl;
    
    @Column(name = "SEED_SALES_CNT")
    private int seedSalesCnt;
 
    @Column(name = "SEED_PRICE")
    private int seedPrice;
}
