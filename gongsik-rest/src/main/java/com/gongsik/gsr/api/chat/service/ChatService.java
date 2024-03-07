package com.gongsik.gsr.api.chat.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		if (!"ENTER".equals(type)) {
			int count = chatRoomRepository.countAll(chkDt, chatRoomNo);
			if (count == 0) {
				map.put("code", "stop");
				return map;
			}
		}else {
			map.put("type", type);
		}
		// 하루 단위 기준으로 조회
		int cnt = currentPage * 15;
		Optional<AccountEntity> account = accountRepository.findByUsrId(usrId);
		if (account.isPresent()) {
			usrNm = account.get().getUsrNm();
		}

		// 채팅 내용 조회
		List<Object[]> entityOptional = chatRoomRepository.findByChatRoomNo(chatRoomNo, cnt);
		System.out.println("방개설 입니다. : " + entityOptional);
		System.out.println("방개설 입니다. : " + entityOptional.isEmpty());
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
		chatRoomEntity.setChatRoomReadChk("Y");
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
		String ChatCrtUsrNm = request.get("chatCrtUsrNm").toString();
		String ChatCrtUsrId = request.get("chatCrtUsrId").toString();
		String ChatInvUsrNm = request.get("chatInvUsrNm").toString();
		String ChatInvUsrId = request.get("chatInvUsrId").toString();
		chatRoomNo = chatRepository.find() + 1;

		ChatEntity chatEntity = new ChatEntity();
		chatEntity.setChatRoomNo(chatRoomNo);
		chatEntity.setChatCrtUsrNm(ChatCrtUsrNm);
		chatEntity.setChatCrtUsrId(ChatCrtUsrId);
		chatEntity.setChatInvUsrId(ChatInvUsrId);
		chatEntity.setChatInvUsrNm(ChatInvUsrNm);
		chatEntity.setUseYn("Y");
		chatEntity.setDelYn("N");
		
		
		ChatEntity chk = chatRepository.save(chatEntity);
		ChatDto chatDto = new ChatDto();
		chatDto.setChatCrtUsrNm(ChatCrtUsrNm);
		chatDto.setChatCrtUsrId(ChatCrtUsrId);
		chatDto.setChatInvUsrNm(ChatInvUsrNm);
		chatDto.setChatInvUsrId(ChatInvUsrId);
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

}
