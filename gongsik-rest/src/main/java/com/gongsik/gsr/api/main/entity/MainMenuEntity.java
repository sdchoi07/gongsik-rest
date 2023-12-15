package com.gongsik.gsr.api.main.entity;


import com.gongsik.gsr.api.main.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
@Table(name = "GS_MENU_MST")
public class MainMenuEntity{
	@Id
	@Column(name = "MENU_NM")
	private String menuNm;
	
	@Column(name = "MENU_GROUP_NO", nullable = false)
    private String menuGroupNo;

    @Column(name = "MENU_ORDER_NO")
    private int menuOrderNo;

    @Column(name = "MENU_PARENT_NO", nullable = true)
    private String menuParentNo;
    
    @Column(name = "MENU_URL", nullable = true)
    private String menuUrl;
   	
    @Embedded
    private CommonEntity comonEntity;

    
    
	
}
