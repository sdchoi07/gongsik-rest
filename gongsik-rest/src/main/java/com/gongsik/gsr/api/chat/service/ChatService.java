package com.gongsik.gsr.api.chat.service;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongsik.gsr.api.account.join.entity.AccountEntity;
import com.gongsik.gsr.api.account.join.repository.AccountRepository;
import com.gongsik.gsr.api.chat.dto.ChatDto;
import com.gongsik.gsr.api.chat.entity.ChatEntity;
import com.gongsik.gsr.api.chat.entity.ChatRoomEntity;
import com.gongsik.gsr.api.chat.repository.ChatRepository;
import com.gongsik.gsr.api.chat.repository.ChatRoomRepository;
import com.gongsik.gsr.global.vo.ResultVO;

@Service
public class ChatService {

	@Autowired
	ChatRepository chatRepository;

	@Autowired
	ChatRoomRepository chatRoomRepository;

	@Autowired
	AccountRepository accountRepository;

	/* 채팅 목록 조회 */
	public Map<String, Object> chatList(String usrId) {
		Map<String, Object> map = new HashMap<>();

		// 채팅 목록 조회
		List<Object[]> chatEntity = chatRepository.findByChatInvUsrIdAndChatCrtUsrId(usrId, usrId);

		List<ChatDto> list = new ArrayList<>();
		for (Object[] entity : chatEntity) {
			ChatDto dto = new ChatDto();
			dto.setChatRoomNo(Integer.parseInt(entity[0].toString()));
			dto.setChatInvUsrId(entity[1].toString());
			dto.setChatInvUsrNm(entity[2].toString());
			dto.setChatCrtUsrId(entity[3].toString());
			dto.setChatCrtUsrNm(entity[4].toString());
			dto.setChatRoomText(entity[5].toString());
			dto.setChatCrtDt(entity[6].toString());
			dto.setChatSendDt(entity[7].toString());
			dto.setChatRoomReadChkNo(Integer.parseInt(entity[8].toString()));
			dto.setUsrNm(entity[9].toString());
			if (!"".equals(dto.getChatSendDt())) {
				String chatDt = dto.getChatSendDt();
				String chatYMD = chatDt.substring(0, 10).replaceAll("-", ".");
				String chatTime = chatDt.substring(11, 16);
				dto.setChatYMD(chatYMD);
				dto.setChatTime(chatTime);
			}

			list.add(dto);
		}

		map.put("result", list);
		return map;
	}

	// 채팅 내용 조회
	public Map<String, Object> chatTextList(Map<String, Object> request) {
		Map<String, Object> map = new HashMap<>();

		String usrId = request.get("usrId").toString();
		int chatRoomNo = Integer.parseInt(request.get("chatRoomNo").toString());

		String usrNm = "";
		String withUsrNm = "";
		String type = "";
		if(request.get("type") != null && !request.get("type").equals("")) {
			type= request.get("type").toString();
		}
		// 내역 조회전 중복 제거
		int currentPage = Integer.parseInt(request.get("currentPage").toString());
		LocalDateTime chkDate = LocalDateTime.now().minusDays(currentPage-1);
		DateTimeFormatter formatChkDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String chkDt = formatChkDate.format(chkDate);
		
		int cnt = currentPage * 15;
		if (!"ENTER".equals(type)) {
			List<Integer> count = chatRoomRepository.countAll(cnt, cnt-15, chatRoomNo);
			if (count.size() == 0) {
				map.put("code", "stop");
				return map;
			}
		}else {
			map.put("type", type);
		}
		// 하루 단위 기준으로 조회
		Optional<AccountEntity> account = accountRepository.findByUsrId(usrId);
		if (account.isPresent()) {
			usrNm = account.get().getUsrNm();
		}

		// 채팅 내용 조회
		List<Object[]> entityOptional = chatRoomRepository.findByChatRoomNo(chatRoomNo, cnt);
		// 채팅 내용이 있을경우
		if (!entityOptional.isEmpty()) {
			List<Object[]> chatRoomEntity = entityOptional;
			List<ChatDto> list = new ArrayList<>();
			String chatDt = "";
			String chatYMD = "";
			String chatTime = "";
			for (int i = chatRoomEntity.size() - 1; i >= 0; i--) {
				Object[] entity = chatRoomEntity.get(i);
				ChatDto dto = new ChatDto();
				dto.setChatRoomNo(Integer.parseInt(entity[0].toString()));
				dto.setChatRoomTextNo(Integer.parseInt(entity[1].toString()));
				dto.setChatRoomSender(entity[2].toString());
				dto.setChatRoomReciver(entity[3].toString());
				dto.setChatRoomText(entity[4].toString());
				dto.setChatRoomReadChk(entity[5].toString());
				dto.setChatSendDt(entity[6].toString());
				if (!"".equals(dto.getChatSendDt())) {
					chatDt = dto.getChatSendDt();
					chatYMD = chatDt.substring(0, 10).replaceAll("-", ".");
					chatTime = chatDt.substring(11, 16);
					dto.setChatYMD(chatYMD);
					dto.setChatTime(chatTime);
				}
				withUsrNm = (usrNm.equals(dto.getChatRoomSender())) ? dto.getChatRoomReciver()
						: dto.getChatRoomSender();
				list.add(dto);
				
				//채팅 읽은거 "Y" 처리
				if(!"scroll".equals(request.get("type")) && !usrNm.equals(dto.getChatRoomSender())&& "N".equals(dto.getChatRoomReadChk())) {
					ChatRoomEntity roomEntity = chatRoomRepository.findByChatRoomNoAndChatRoomTextNo(dto.getChatRoomNo(), dto.getChatRoomTextNo());
					roomEntity.setChatRoomReadChk("Y");
					chatRoomRepository.save(roomEntity);
					
				}
			}
			map.put("code", "success");
			map.put("result", list);
			map.put("usrNm", usrNm);
			map.put("withUsrNm", withUsrNm);
			
			
//			//첫 챗팅일경우(초대받은)
//			int withCnt = chatRoomRepository.countAll(chatRoomNo,usrNm);
//			if(withCnt == 1 ) {
//				map.put("withCnt", withCnt);
//			}
		} else {
			// 방만 개설된 경우
			withUsrNm = chatRepository.findByChatRoomNo(chatRoomNo);
			map.put("code", "fail");
			map.put("msg", "채팅 내역이 없습니다.");
			map.put("chatRoomNo", chatRoomNo);
			map.put("withUsrNm", withUsrNm);
			chkDt = chkDt.substring(0, 10).replaceAll("-", ".");
			map.put("chatYMD", chkDt);
		}

		return map;
	}

	/* 채팅 내역 저장 */
	@Transactional
	public ResultVO chatTextSave(Map<String, Object> request) {
		ResultVO resultVo = new ResultVO();
		int chatRoomNo = Integer.parseInt(request.get("chatRoomNo").toString());
		String chatRoomSender = request.get("sender").toString();
		String chatRoomReciver = request.get("reciver").toString();
		String chatRoomText = request.get("message").toString();
		String sendDt = request.get("sendDt").toString();
		int chatRoomTextNo = chatRoomRepository.findByChatRoomNo(chatRoomNo) + 1;

		ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
		chatRoomEntity.setChatRoomNo(chatRoomNo);
		chatRoomEntity.setChatRoomSender(chatRoomSender);
		chatRoomEntity.setChatRoomReciver(chatRoomReciver);
		chatRoomEntity.setChatRoomTextNo(chatRoomTextNo);
		chatRoomEntity.setChatRoomText(chatRoomText);
		chatRoomEntity.setChatSendDt(sendDt);
		chatRoomEntity.setChatRoomReadChk("N");
		ChatRoomEntity chk = chatRoomRepository.save(chatRoomEntity);
		if (chk != null) {
			resultVo.setCode("success");
			resultVo.setMsg("채팅 내역 저장 성공");
		} else {
			resultVo.setCode("fail");
			resultVo.setMsg("채팅내역 저장 실패");
		}
		
		return resultVo;
	}

	/* 채팅 가능 유저 조회 */
	public Map<String, Object> accountLists() {
		Map<String, Object> map = new HashMap<>();
		Optional<List<AccountEntity>> account = accountRepository.findByChatYn("Y");

		if (account.isPresent()) {
			List<AccountEntity> accountEntity = account.get();
			List<ChatDto> list = new ArrayList<>();
			for (AccountEntity entity : accountEntity) {
				ChatDto chatDto = new ChatDto();
				chatDto.setChatInvUsrNm(entity.getUsrNm());
				chatDto.setChatInvUsrId(entity.getUsrId());
				list.add(chatDto);
			}
			map.put("result", list);
			map.put("cnt", list.size());
			map.put("code", "success");
		} else {
			map.put("code", "fail");
			map.put("msg", "회원이 없습니다.");
		}
		return map;
	}

	/* 방 생성 하기 */
	@Transactional
	public ResultVO chatCreatRoom(Map<String, Object> request) {
		ResultVO resultVo = new ResultVO();
		int chatRoomNo = 0;
		String chatCrtUsrNm = request.get("chatCrtUsrNm").toString();
		String chatCrtUsrId = request.get("chatCrtUsrId").toString();
		String chatInvUsrNm = request.get("chatInvUsrNm").toString();
		String chatInvUsrId = request.get("chatInvUsrId").toString();
		chatRoomNo = chatRepository.find() + 1;
		
		int cnt = chatRepository.findByChatCrtUsrNmAndChatCrtUsrIdAndChatInvUsrNmAndChatInvUsrId(chatCrtUsrNm, chatCrtUsrId, chatInvUsrNm, chatInvUsrId);
		if(cnt == 1) {
			resultVo.setCode("fail");
			resultVo.setMsg("이미 대화중 입니다.");
			System.out.print("뭐애 왜 무시해");
			return resultVo;
		}
		ChatEntity chatEntity = new ChatEntity();
		chatEntity.setChatRoomNo(chatRoomNo);
		chatEntity.setChatCrtUsrNm(chatCrtUsrNm);
		chatEntity.setChatCrtUsrId(chatCrtUsrId);
		chatEntity.setChatInvUsrId(chatInvUsrId);
		chatEntity.setChatInvUsrNm(chatInvUsrNm);
		chatEntity.setUseYn("Y");
		chatEntity.setDelYn("N");
		
		
		ChatEntity chk = chatRepository.save(chatEntity);
		ChatDto chatDto = new ChatDto();
		chatDto.setChatCrtUsrNm(chatCrtUsrNm);
		chatDto.setChatCrtUsrId(chatCrtUsrId);
		chatDto.setChatInvUsrNm(chatInvUsrNm);
		chatDto.setChatInvUsrId(chatInvUsrId);
		chatDto.setChatRoomNo(chatRoomNo);
		if (chk != null) {
			resultVo.setCode("success");
			resultVo.setMsg("성공");
			resultVo.setObject(chatDto);
		} else {
			resultVo.setCode("fail");
			resultVo.setMsg("채팅 방 생성 실패");
		}
		return resultVo;
	}
	
	/* 방 삭제 하기 */
	@Transactional
	public ResultVO delCahtRoom(int chatRoomNo) {
		ResultVO resultVo = new ResultVO();
		LocalDateTime chkDate = LocalDateTime.now();
		DateTimeFormatter formatChkDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String delDt = formatChkDate.format(chkDate);
		ChatEntity chatEntity = chatRepository.findByChatRoomNoAndUseYnAndDelYn(chatRoomNo, "Y" ,"N");
		chatEntity.setChatRoomNo(chatRoomNo);
		chatEntity.setChatCrtUsrNm(chatEntity.getChatCrtUsrNm());
		chatEntity.setChatCrtUsrId(chatEntity.getChatCrtUsrId());
		chatEntity.setChatInvUsrId(chatEntity.getChatInvUsrId());
		chatEntity.setChatInvUsrNm(chatEntity.getChatInvUsrNm());
		chatEntity.setUseYn("N");
		chatEntity.setDelYn("Y");
		
		
		ChatEntity chk = chatRepository.save(chatEntity);
		if (chk != null) {
			resultVo.setCode("success");
			resultVo.setMsg("채팅방을 나갔습니다.");
		} else {
			resultVo.setCode("fail");
			resultVo.setMsg("다시 한번 나가기 눌러 주세요.");
		}
		return resultVo;
	}


}
