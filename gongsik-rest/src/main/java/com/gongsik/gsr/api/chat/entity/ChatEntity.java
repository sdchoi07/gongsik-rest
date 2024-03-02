package com.gongsik.gsr.api.chat.entity;

import org.springframework.data.annotation.CreatedDate;

import com.gongsik.gsr.api.common.entity.CommonEntity;
import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryEntity;
import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryMultikey;

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
@Table(name = "GS_CHAT_INF")
public class ChatEntity {
	
	@Id
	@Column(name = "CHAT_ROOM_NO", nullable = false)
	private long chatRoomNo;
	
	@Column(name = "CHAT_INV_USR_ID")
	private String chatInvUsrId;
	
	@Column(name = "CHAT_INV_USR_NM")
	private String chatInvUsrNm;
	
	@Column(name = "CHAT_CRT_USR_ID")
	private String chatCrtUsrId;
	
	@Column(name = "CHAT_CRT_USR_NM")
	private String chatCrtUsrNm;
	
	@CreatedDate
    @Column(name = "CHAT_CRT_DT",  insertable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private String chatCrtDt;
	
	@Column(name = "CHAT_DEL_DT")
	private String chatDelDt;
	
	@Column(name = "USE_YN")
	private String useYn;
	
	@Column(name = "DEL_YN")
	private String delYn;

	@Embedded
    private CommonEntity comonEntity;
}
