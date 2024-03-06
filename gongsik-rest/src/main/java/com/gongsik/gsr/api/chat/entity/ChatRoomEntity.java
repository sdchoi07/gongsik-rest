package com.gongsik.gsr.api.chat.entity;

import org.springframework.data.annotation.CreatedDate;

import com.gongsik.gsr.api.common.entity.CommonEntity;
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
@IdClass(ChatRoomMultiKey.class)	
@Table(name = "GS_CHAT_ROOM_INF")
public class ChatRoomEntity {
	
	@Id
	@Column(name = "CHAT_ROOM_NO", nullable = false)
	private long chatRoomNo;
	
	@Id
	@Column(name = "CHAT_ROOM_TEXT_NO")
	private int chatRoomTextNo;
	
	@Column(name = "CHAT_ROOM_SENDER")
	private String chatRoomSender;
	
	@Column(name = "CHAT_ROOM_RECIVER")
	private String chatRoomReciver;
	
	@Column(name = "CHAT_ROOM_TEXT")
	private String chatRoomText;

	@Column(name = "CHAT_ROOM_READ_CHK")
    private String chatRoomReadChk;
	
    @Column(name = "CHAT_SEND_DT")
	private String chatSendDt;
	
	@Embedded
    private CommonEntity comonEntity;

}
