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
@Table(name = "GS_PRODUCT_INF")
public class ProductEntity {

	
	@Column(name = "PRODUCT_NM")
    private String productNm;
	
	@Id
	@Column(name = "PRODUCT_NO")
    private String productNo;

    @Column(name = "PRODUCT_TEXT")
    private String productText;

    @Column(name = "PRODUCT_URL")
    private String productUrl;
    
    @Column(name = "PRODUCT_SALES_CNT")
    private int productSalesCnt;
 
    @Column(name = "PRODUCT_PRICE")
    private int productPrice;
    
}
