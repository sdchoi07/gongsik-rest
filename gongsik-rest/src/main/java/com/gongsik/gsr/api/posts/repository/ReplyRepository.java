package com.gongsik.gsr.api.posts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.posts.entity.PostsEntity;
import com.gongsik.gsr.api.posts.entity.ReplyEntity;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{
	
	@Query(value=
		      "	SELECT IFNULL(MAX(REPLY_NO),0) AS REPLY_NO														 "
			+ "	FROM GS_REPLY_INF A																				 "
			+ " WHERE POST_NO = :postNo 																	         "
																															,nativeQuery=true)
	int findOneByPostNo(@Param("postNo")int postNo);

	@Query(value=
		      "	SELECT IFNULL(MAX(REPLY_MINI_NO),0) AS REPLY_MINI_NO														 "
			+ "	FROM GS_REPLY_INF A																				 "
			+ " WHERE POST_NO = :postNo 																	         "
			+ "   AND REPLY_NO = :replyNo																			 "
																															,nativeQuery=true)
	int findOneByPostNoByReplyNo(@Param("postNo")int postNo, @Param("replyNo")int reviewNo);

	List<ReplyEntity> findByPostNoAndDelYnOrderByReplyNo(int postsNo, String string);

	ReplyEntity findByPostNoAndReplyNoAndReplyMiniNo(int postNo, int replyNo, int replyMiniNo);

	List<ReplyEntity> findByPostNoAndReplyNo(int postNo, int replyNo);


}
