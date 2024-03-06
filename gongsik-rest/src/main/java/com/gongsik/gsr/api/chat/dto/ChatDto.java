package com.gongsik.gsr.api.chat.dto;

import com.gongsik.gsr.api.common.dto.CommonDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "ChatDto")
public class ChatDto extends CommonDto{
	
	/* 채팅 테이블 */
	private long chatRoomNo;
	private String chatInvUsrId;
	private String chatInvUsrNm;
	private String chatCrtUsrId;
	private String chatCrtUsrNm;
    private String chatCrtDt;
	private String chatDelDt;
	private String useYn;
	private String delYn;
	private String chatYMD;
	private String chatTime;
	private String usrNm;
	
	/* 채팅 방 테이블 */
	private int chatRoomTextNo;
	private String chatRoomSender;
	private String chatRoomReciver;
	private String chatRoomText;
    private String chatRoomReadChk;
	private String chatSendDt;
	private String withUsrNm;

}
