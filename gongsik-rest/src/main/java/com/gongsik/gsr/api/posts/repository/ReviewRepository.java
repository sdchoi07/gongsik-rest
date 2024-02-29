package com.gongsik.gsr.api.posts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.posts.entity.PostsEntity;
import com.gongsik.gsr.api.posts.entity.ReviewEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{
	
	@Query(value=
		      "	SELECT IFNULL(MAX(REVIEW_NO),0) AS REVIEW_NO														 "
			+ "	FROM GS_REVIEW_INF A																				 "
			+ " WHERE POST_NO = :postNo 																	         "
																															,nativeQuery=true)
	int findOneByPostNo(@Param("postNo")int postNo);

	@Query(value=
		      "	SELECT IFNULL(MAX(REVIEW_MINI_NO),0) AS REVIEW_MINI_NO														 "
			+ "	FROM GS_REVIEW_INF A																				 "
			+ " WHERE POST_NO = :postNo 																	         "
			+ "   AND REVIEW_NO = :reviewNo																			 "
																															,nativeQuery=true)
	int findOneByPostNoByReviewNo(@Param("postNo")int postNo, @Param("reviewNo")int reviewNo);

	List<ReviewEntity> findByPostNoAndDelYnOrderByReviewNo(int postsNo, String string);

}
