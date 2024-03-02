package com.gongsik.gsr.api.chat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.chat.dto.ChatDto;
import com.gongsik.gsr.api.chat.entity.ChatEntity;
import com.gongsik.gsr.api.chat.entity.ChatRoomEntity;
import com.gongsik.gsr.api.chat.repository.ChatRepository;

@Service
public class ChatService {
	
	@Autowired
	ChatRepository chatRepository;
	
	/* 채팅 목록 조회 */
	public Map<String, Object> chatList(String usrId) {
		Map<String, Object> map = new HashMap<>();
		

		//채팅 목록 조회
		List<Object[]> chatEntity = chatRepository.findByChatInvUsrIdAndChatCrtUsrId(usrId, usrId);
		
		List<ChatDto> list = new ArrayList<>();
		for(Object[] entity : chatEntity) {
			ChatDto dto = new ChatDto();
			dto.setChatRoomNo(Integer.parseInt(entity[0].toString()));
			dto.setChatInvUsrId(entity[1].toString());
			dto.setChatInvUsrNm(entity[2].toString());
			dto.setChatCrtUsrId(entity[3].toString());
			dto.setChatCrtUsrNm(entity[4].toString());
//			dto.setChatSendDt(entity[5].toString());
			list.add(dto);
		}
		
		map.put("result", list);
		return map;
	}

}
