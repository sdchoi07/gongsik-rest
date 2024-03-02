package com.gongsik.gsr.api.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.chat.entity.ChatEntity;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

	@Query(value = "																		"
			+ "		SELECT																	"
			+ "						 A.CHAT_ROOM_NO 										"
			+ "      				,A.CHAT_INV_USR_ID 										"
			+ "      			    ,A.CHAT_INV_USR_NM 										"
			+ "      				,A.CHAT_CRT_USR_ID 										"
			+ "      				,A.CHAT_CRT_USR_NM 										"
			+ "      				,B.CHAT_SEND_DT  										"
			+ "      				,B.CHAT_ROOM_READ_CHK 									"
			+ "		  FROM GS_CHAT_INF A													"
			+ "       LEFT JOIN GS_CHAT_ROOM_INF B ON A.CHAT_ROOM_NO = B.CHAT_ROOM_NO 		"
			+ "      WHERE (A.CHAT_INV_USR_ID = :invUsrId									"
			+ "        OR A.CHAT_CRT_USR_ID = :crtUsrId)									"
			+ "       AND A.USE_YN = 'Y'													"
			+ "       AND A.DEL_YN = 'N'													"   , nativeQuery = true)
	List<Object[]> findByChatInvUsrIdAndChatCrtUsrId(@Param("invUsrId") String invUsrId,
			@Param("crtUsrId") String crtUsrId);
}
