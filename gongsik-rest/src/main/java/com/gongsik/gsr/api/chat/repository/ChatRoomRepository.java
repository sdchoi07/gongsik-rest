package com.gongsik.gsr.api.chat.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.chat.entity.ChatEntity;
import com.gongsik.gsr.api.chat.entity.ChatRoomEntity;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long>{
	
	
	@Query(value=
		      "	SELECT IFNULL(MAX(CHAT_ROOM_TEXT_NO),0) AS CHAT_ROOM_TEXT_NO												 "
			+ "	FROM GS_CHAT_ROOM_INF A																				         "
			+ " WHERE CHAT_ROOM_NO = :chatRoomNo 																	         "
																															,nativeQuery=true)
	int findByChatRoomNo(@Param("chatRoomNo")int chatRoomNo);
	
	@Query(value=
		      "			SELECT   CHAT_ROOM_NO 																		"
		      + "       		,CHAT_ROOM_TEXT_NO 																	"
		      + "       		,CHAT_ROOM_SENDER 																	"
		      + "       		,CHAT_ROOM_RECIVER 																	"
		      + "       		,CHAT_ROOM_TEXT 																	"
		      + "       		,CHAT_ROOM_READ_CHK 																"
		      + "       		,CHAT_SEND_DT 																		"
		      + "		FROM GS_CHAT_ROOM_INF A																	    "
		      + "		WHERE CHAT_ROOM_NO = :chatRoomNo 															"
		      + "  		AND   CHAT_SEND_DT <= :endDate															"
		      + "  		AND   CHAT_SEND_DT >= :startDate 														        "
																															,nativeQuery=true)
	Optional<List<Object[]>> findByChatRoomNoAndChatSendDtAndChatSendDtOrderByChatSendDt(@Param("chatRoomNo")int chatRoomNo,
			@Param("startDate")String startDate, @Param("endDate")String endDate);
	
	@Query(value=
		      "	SELECT COUNT(*) 																							 "
			+ "	FROM GS_CHAT_ROOM_INF A																				         "
			+ " WHERE CHAT_ROOM_NO = :chatRoomNo 																	         "
			+ "   AND CHAT_SEND_DT <= :chkDt																					 "
																															,nativeQuery=true)
	int countAll(@Param("chkDt")String chkDt,@Param("chatRoomNo")int chatRoomNo);



}
