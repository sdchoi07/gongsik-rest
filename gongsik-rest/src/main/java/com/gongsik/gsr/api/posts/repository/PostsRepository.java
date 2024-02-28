package com.gongsik.gsr.api.posts.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.posts.entity.PostsEntity;

@Repository
public interface PostsRepository extends JpaRepository<PostsEntity, Long>{
	
	@Query(value=
		      "	SELECT IFNULL(MAX(POSTS_NO),0) AS POSTS_NO "
			+ "	FROM GS_POSTS_INF A	"
																															,nativeQuery=true)
	int findOne();

	List<PostsEntity> findTop3ByOrderByPostsViewCntDesc();

	int countByDelYn(String string);

	int countByPostsGubunAndDelYn(String gubun, String string);

	PostsEntity findByPostsNo(int postsNo);

	List<PostsEntity> findAllByDelYn(String string, Pageable pageable);

	List<PostsEntity> findByPostsGubunAndDelYn(String gubun, String string, Pageable pageable);

	List<PostsEntity> findByPostsUsrIdAndDelYn(String usrId, String string, Pageable pageable);

	int countByPostsUsrIdAndDelYn(String usrId, String string);

}
