package com.gongsik.gsr.api.main;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CommonEntity {
	@CreatedBy
    @Column(name = "REG_ID")
    private String regId;
    
    @Column(name = "REG_IP")
    private String regIp;
    @CreatedDate
    @Column(name = "REG_DT", insertable = false, updatable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private String regDt;
    @LastModifiedBy
    @Column(name = "CHG_ID")
    private String chgId;
    
    @Column(name = "CHG_IP")
    private String chgIp;
    @LastModifiedDate
    @Column(name = "CHG_DT", insertable = false, updatable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    private String chgDt;
}
