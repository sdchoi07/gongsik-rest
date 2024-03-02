package com.gongsik.gsr.api.chat.entity;

import java.io.Serializable;

import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryMultikey;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor		//전체생성자
@NoArgsConstructor		//기본 생성자
@EqualsAndHashCode		//equals, hashCode
@Data
public class ChatRoomMultiKey implements Serializable{

	@Column(name = "CHAT_ROOM_NO", nullable = false)
	private long chatRoomNo;
	
	@Column(name = "CHAT_ROOM_TEXT_NO")
	private String chatRoomTextNo;

}
