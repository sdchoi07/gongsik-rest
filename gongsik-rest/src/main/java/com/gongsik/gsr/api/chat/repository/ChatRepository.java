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
			+ "                     ,CASE WHEN C.CHAT_SEND_DT IS NULL THEN CHAT_CRT_DT ELSE C.CHAT_SEND_DT END              "
	        + "                     ,IFNULL(D.CHAT_ROOM_READ_CHK,0) AS CHAT_ROOM_READ_CHK   "
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
			+ "         (SELECT  A.CHAT_ROOM_NO												"
			+ "					,A.CHAT_ROOM_RECIVER 										"
			+ "                 ,A.CHAT_ROOM_TEXT											"
			+ "                 ,A.CHAT_SEND_DT												"
			+ "                 ,A.CHAT_ROOM_SENDER											"
			+ "            FROM GS_CHAT_ROOM_INF A 										    "
			+ "           WHERE (CHAT_SEND_DT, CHAT_ROOM_TEXT_NO) 							"
			+ "								 IN ( SELECT MAX(CHAT_SEND_DT), MAX(CHAT_ROOM_TEXT_NO)  "
			+ "								       FROM GS_CHAT_ROOM_INF                    "
			+ "									   GROUP BY CHAT_ROOM_NO					"
			+ "                                    ORDER BY CHAT_SEND_DT)                   "
			+ "            						   GROUP BY CHAT_ROOM_NO					"
			+ "											   ,CHAT_ROOM_RECIVER				"
			+ "											   ,CHAT_ROOM_TEXT					"
			+ "											   ,CHAT_SEND_DT					"
			+ "											   ,CHAT_ROOM_SENDER)				"
			+ "              C ON A.CHAT_ROOM_NO = C.CHAT_ROOM_NO 							"
			+ "     LEFT JOIN																"
			+ "            (     SELECT													    "
			+ "                          COUNT(CHAT_ROOM_READ_CHK) CHAT_ROOM_READ_CHK		"
			+ "                         ,CHAT_ROOM_NO										"
			+ "                         ,CHAT_ROOM_RECIVER                					"
			+ "            	      FROM  GS_CHAT_ROOM_INF                                    "
			+ "                  WHERE  CHAT_ROOM_READ_CHK = 'N'                            "
			+ "                  GROUP  BY CHAT_ROOM_NO										"
			+ "                        ,CHAT_ROOM_RECIVER  )D 								"
			+ "                ON A.CHAT_ROOM_NO = D.CHAT_ROOM_NO AND USR_NM = D.CHAT_ROOM_RECIVER    			    "
			+ "      WHERE (A.CHAT_INV_USR_ID = :invUsrId									"
			+ "        OR A.CHAT_CRT_USR_ID = :crtUsrId)									"
			+ "       AND A.USE_YN = 'Y'													"
			+ "       AND A.DEL_YN = 'N'													"
			+ "    ORDER BY C.CHAT_SEND_DT DESC, A.CHAT_ROOM_NO DESC, A.CHAT_CRT_DT							"   , nativeQuery = true)
	List<Object[]> findByChatInvUsrIdAndChatCrtUsrId(@Param("invUsrId") String invUsrId,
			@Param("crtUsrId") String crtUsrId);
	
	@Query(value=
		      "	SELECT IFNULL(MAX(CHAT_ROOM_NO),0) AS CHAT_ROOM_NO												         "
			+ "	FROM GS_CHAT_INF A																				         "
																															,nativeQuery=true)
	int find();
	
	@Query(value=
		      "	SELECT DISTINCT(CHAT_INV_USR_NM) AS CHAT_INV_USR_NM												         "
			+ "	FROM GS_CHAT_INF A																						 "
			+ "	WHERE CHAT_ROOM_NO = :chatRoomNo																		 "
			+ "  AND DEL_YN = 'N'																						 "
			+ "  AND USE_YN = 'Y'											         									 "
																															,nativeQuery=true)
	String findByChatRoomNo(@Param("chatRoomNo")int chatRoomNo);

	ChatEntity findByChatRoomNoAndUseYnAndDelYn(int chatRoomNo, String string, String string2);
	
	@Query(value=
		      "    SELECT															"
		      + "  (SELECT count(*)													"
		      + "   FROM GS_CHAT_INF A												"
		      + "   WHERE CHAT_CRT_USR_ID = :chatCrtUsrId						    "
		      + "     AND CHAT_CRT_USR_NM = :chatCrtUsrNm							"
		      + "     AND CHAT_INV_USR_ID = :chatInvUsrId						    "
		      + "     AND CHAT_INV_USR_NM = :chatInvUsrNm							"
		      + "     AND USE_YN = 'Y'												"
		      + "     AND DEL_YN = 'N') +											"
		      + "  (SELECT count(*)													"
		      + "   FROM GS_CHAT_INF A												"
		      + "   WHERE CHAT_CRT_USR_ID = :chatInvUsrId					        "
		      + "     AND CHAT_CRT_USR_NM = :chatInvUsrNm							"
		      + "     AND CHAT_INV_USR_ID = :chatCrtUsrId					  		"
		      + "     AND CHAT_INV_USR_NM = :chatCrtUsrNm							"
		      + "	  AND USE_YN = 'Y'												"
		      + "     AND DEL_YN = 'N') AS total_count				         									 "
																															,nativeQuery=true)
	int findByChatCrtUsrNmAndChatCrtUsrIdAndChatInvUsrNmAndChatInvUsrId(@Param("chatCrtUsrNm")String chatCrtUsrNm, @Param("chatCrtUsrId")String chatCrtUsrId,
			@Param("chatInvUsrNm")String chatInvUsrNm, @Param("chatInvUsrId")String chatInvUsrId);

	@Query(value=
		      "	SELECT IFNULL(COUNT(CHAT_ROOM_READ_CHK),0) AS CHAT_ROOM_READ_CHK												         "
			+ "	FROM GS_CHAT_ROOM_INF A																"
			+ " WHERE CHAT_ROOM_RECIVER = :usrNm												"
			+ " AND  CHAT_ROOM_READ_CHK = :chatRoomReadChk								         "
																															,nativeQuery=true)
	int countReadChk(@Param("usrNm")String usrNm, @Param("chatRoomReadChk")String chatRoomReadChk);
	

}
