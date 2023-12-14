package com.gongsik.gsr.api.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Getter
@Setter
@Table(name = "GS_MENU_MST")
public class MainMenuEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
   	
    @Column(name = "REG_ID")
    private String regId;
    
    @Column(name = "REG_IP")
    private String regIp;
    
    @Column(name = "REG_DT")
    private String regDt;
    
    @Column(name = "CHG_ID")
    private String chgId;
    
    @Column(name = "CHG_IP")
    private String chgIp;
    
    @Column(name = "CHG_DT")
    private String chgDt;
    
    
	
}
