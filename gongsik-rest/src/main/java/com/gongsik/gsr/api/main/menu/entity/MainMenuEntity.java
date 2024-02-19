package com.gongsik.gsr.api.main.menu.entity;


import com.gongsik.gsr.api.common.entity.CommonEntity;
import com.querydsl.core.annotations.QueryEntity;

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
	
	@Column(name = "MENU_NO", nullable = false)
    private int menuNo;

	@Column(name = "MENU_GROUP_NO", nullable = false)
    private String menuGroupNo;

    @Column(name = "MENU_ORDER_NO" )
    private int menuOrderNo;

    @Column(name = "MENU_LEVEL_NO", nullable = true)
    private int menuLevelNo;
    
    @Column(name = "MENU_URL", nullable = true)
    private String menuUrl;
    
    @Column(name = "MENU_ITEM_NO")
    private String menuItemNo;
   	
    @Embedded
    private CommonEntity comonEntity;

    
    
	
}
