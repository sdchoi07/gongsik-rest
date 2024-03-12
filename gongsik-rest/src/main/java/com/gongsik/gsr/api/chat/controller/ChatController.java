package com.gongsik.gsr.api.chat.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongsik.gsr.api.chat.service.ChatService;
import com.gongsik.gsr.global.config.WebClients;
import com.gongsik.gsr.global.vo.ResultVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat Controller", description = "채팅")
@RequiredArgsConstructor
public class ChatController {

	@Autowired
	private ChatService chatService;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	

	@GetMapping("/chatList/{usrId}")
	@Operation(summary = "채팅 목록 조회", description = "채팅 목록 조회 하기")
	@Parameters({ @Parameter(description = "사용자 아이디", name = "usrId", example = "test"), })
	public ResponseEntity<Map<String, Object>> chatList(@PathVariable("usrId") String usrId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = chatService.chatList(usrId);
		return ResponseEntity.ok(map);
	}

	@PostMapping("/chatTextList")
	@Operation(summary = "채팅 내용 조회", description = "채팅 내용 조회 하기")
	@Parameters({ @Parameter(description = "채팅방 번호", name = "chatRoomNo", example = "123"), })
	public ResponseEntity<Map<String, Object>> chatTextList(@RequestBody Map<String, Object> request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = chatService.chatTextList(request);
		return ResponseEntity.ok(map);
	}

	@PostMapping("/chatTextSave")
	@Operation(summary = "채팅 내용 저장", description = "채팅 내용 저장 하기")
	@Parameters({ @Parameter(description = "채팅방 번호", name = "chatRoomNo", example = "123"),
			@Parameter(description = "수신자", name = "chatRoomReciver", example = "id"),
			@Parameter(description = "송신자", name = "chatRoomSender", example = "id"),
			@Parameter(description = "내용", name = "chatRoomText", example = "content") })
	public ResponseEntity<ResultVO> chatTextSave(@RequestBody Map<String, Object> request) {
		ResultVO resultVo = new ResultVO();
		resultVo = chatService.chatTextSave(request);
		
		// B의 클라이언트 서버에 SSE 이벤트를 전송하는 POST 요청
		WebClients webClients = new WebClients();
		Mono<Object> responseMono = webClients.callApi(Object.class, "/alramMsg", "test@gmail.com");
		// 결과 값 가져오기
		
		responseMono.subscribe(response -> {
			System.out.println("여기 오냐 ?");
		    // 반환된 응답(response)을 이용하여 처리
		}, error -> {
		    // 에러 처리
		});
		return ResponseEntity.ok(resultVo);
	}
	
	@GetMapping("/accountLists/{usrId}")
	@Operation(summary = "채팅 회원 조회", description = "채팅 회원 조회 하기")
	public ResponseEntity<Map<String, Object>> accountLists(@PathVariable("usrId") String usrId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = chatService.accountLists(usrId);
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/chatCreatRoom")
	@Operation(summary = "채팅 방 저장", description = "채팅 방 저장 하기")
	@Parameters({
			@Parameter(description = "수신자", name = "chatRoomReciver", example = "id"),
			@Parameter(description = "송신자", name = "chatRoomSender", example = "id")})
	public ResponseEntity<ResultVO> chatCreatRoom(@RequestBody Map<String, Object> request) {
		ResultVO resultVo = new ResultVO();
		resultVo = chatService.chatCreatRoom(request);
		return ResponseEntity.ok(resultVo);
	}
	
	@GetMapping("/delCahtRoom/{chatRoomNo}")
	@Operation(summary = "채팅 방 저장", description = "채팅 방 저장 하기")
	@Parameters({
			@Parameter(description = "방번호", name = "chatRoomSender", example = "2")})
	public ResponseEntity<ResultVO> delCahtRoom(@PathVariable("chatRoomNo") int chatRoomNo) {
		ResultVO resultVo = new ResultVO();
		resultVo = chatService.delCahtRoom(chatRoomNo);
		return ResponseEntity.ok(resultVo);
	}

}
