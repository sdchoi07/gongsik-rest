package com.gongsik.gsr.api.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.chat.entity.ChatEntity;
import com.gongsik.gsr.api.chat.entity.ChatRoomEntity;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

	@Query(value = "																		"
			+ "		SELECT																	"
			+ "						 A.CHAT_ROOM_NO 										"
			+ "      				,A.CHAT_INV_USR_ID 										"
			+ "      			    ,A.CHAT_INV_USR_NM 										"
			+ "      				,A.CHAT_CRT_USR_ID 										"
			+ "      				,A.CHAT_CRT_USR_NM 										"
			+ "                     ,IFNULL(C.CHAT_ROOM_TEXT,'') AS CHAT_ROOM_TEXT			"
			+ "						,A.CHAT_CRT_DT 											" 
	        + "                     ,CASE WHEN C.CHAT_SEND_DT IS NULL THEN A.CHAT_CRT_DT    "
	        + "                        ELSE IFNULL(C.CHAT_SEND_DT, '') END  AS CHAT_SEND_DT "  
			+ "      				,IFNULL(C.CHAT_ROOM_READ_CHK,'') AS CHAT_BOOK_READ_CHK	"
			+ "						,B.USR_NM                                               "
			+ "		  FROM GS_CHAT_INF A													"
			+ "       JOIN(																	"
			+ "          SELECT  USR_ID														"
			+ "                 ,USR_NM														"
			+ "            FROM GS_ACCOUNT_INF 												"
			+ "           WHERE USR_ID = :invUsrId) B 								        "
			+ "              ON (B.USR_ID = A.CHAT_INV_USR_ID 								"
			+ "              OR B.USR_ID = A.CHAT_CRT_USR_ID) 								"
			+ "       LEFT JOIN 															"
			+ "         (SELECT  CHAT_ROOM_NO												"
			+ "                 ,CHAT_ROOM_TEXT												"
			+ "                 ,CHAT_SEND_DT												"
			+ "                 ,CHAT_ROOM_READ_CHK											"
			+ "            FROM GS_CHAT_ROOM_INF 										    "
			+ "           WHERE CHAT_SEND_DT = ( SELECT MAX(CHAT_SEND_DT)                   "
			+ "								       FROM GS_CHAT_ROOM_INF))  			    "
			+ "              C ON A.CHAT_ROOM_NO = C.CHAT_ROOM_NO 							"
			+ "      WHERE (A.CHAT_INV_USR_ID = :invUsrId									"
			+ "        OR A.CHAT_CRT_USR_ID = :crtUsrId)									"
			+ "       AND A.USE_YN = 'Y'													"
			+ "       AND A.DEL_YN = 'N'													"
			+ "    ORDER BY A.CHAT_ROOM_NO, C.CHAT_SEND_DT DESC, A.CHAT_CRT_DT							"   , nativeQuery = true)
	List<Object[]> findByChatInvUsrIdAndChatCrtUsrId(@Param("invUsrId") String invUsrId,
			@Param("crtUsrId") String crtUsrId);
	
	@Query(value=
		      "	SELECT IFNULL(MAX(CHAT_ROOM_NO),0) AS CHAT_ROOM_NO												         "
			+ "	FROM GS_CHAT_INF A																				         "
																															,nativeQuery=true)
	int find();
	

}
