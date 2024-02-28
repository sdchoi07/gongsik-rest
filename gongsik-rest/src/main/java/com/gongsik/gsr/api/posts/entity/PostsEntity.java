package com.gongsik.gsr.api.posts.entity;

import org.springframework.data.annotation.CreatedDate;

import com.gongsik.gsr.api.common.entity.CommonEntity;
import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryEntity;
import com.gongsik.gsr.api.mypage.delivery.entity.DeliveryMultikey;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data	
@Table(name = "GS_POSTS_INF")
public class PostsEntity {
	
	@Id
	@Column(name = "POSTS_SEQ", nullable = false)
	private long postsSeq;
	
	@Column(name = "POSTS_USR_ID")
	private String postsUsrId;
	
	@Column(name = "POSTS_USR_NM")
	private String postsUsrNm;
	
	@Column(name = "POSTS_NM")
	private String postsNm;
	
	@Column(name = "POSTS_NO")
	private int postsNo;

	@Column(name = "POSTS_URL")
    private String postsUrl;
	
	@Column(name = "POSTS_TEXT")
	private String postsText;

	@Column(name = "POSTS_GUBUN")
	private String postsGubun;
	
	@Column(name = "POSTS_VIEW_CNT")
	private int postsViewCnt;
	
	@CreatedDate
	@Column(name = "POSTS_DT", insertable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
	private String postsDt;
	
	@Column(name = "POSTS_MOD_DT")
	private String postSModDt;
	
	@Column(name = "DEL_YN")
	private String delYn;

	@Embedded
    private CommonEntity comonEntity;
}
