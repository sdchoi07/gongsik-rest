package com.gongsik.gsr.api.chat.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.chat.service.ChatService;
import com.gongsik.gsr.api.mypage.delivery.service.DeliveryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat Controller", description = "채팅")
@RequiredArgsConstructor
public class ChatController {
	
	@Autowired
	private ChatService chatService;
	
	@GetMapping("/chatList/{usrId}")
	@Operation(summary = "채팅 목록 조회", description = "채팅 목록 조회 하기")
	@Parameters({
        @Parameter(description = "사용자 아이디", name = "usrId", example = "test"),
	})
	public ResponseEntity<Map<String, Object>> chatList(@PathVariable("usrId") String usrId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = chatService.chatList(usrId);
		return ResponseEntity.ok(map);
	}
}
