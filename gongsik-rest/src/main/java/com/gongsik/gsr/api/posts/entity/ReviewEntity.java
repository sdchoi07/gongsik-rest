package com.gongsik.gsr.api.posts.entity;

import org.springframework.data.annotation.CreatedDate;

import com.gongsik.gsr.api.common.entity.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "GS_REVIEW_INF")
public class ReviewEntity {
	
	@Id
	@Column(name = "REVIEW_SEQ", nullable = false)
	private long postsSeq;
	
	@Column(name = "REVIEW_NO")
	private int reviewNo;
	
	@Column(name = "POST_NO")
	private int postNo;
	
	@Column(name = "REVIEW_ID")
	private String reviewId;
	
	@Column(name = "REVIEW_NM")
	private String reviewNm;

	@Column(name = "REVIEW_TEXT")
    private String reviewText;
	
	@Column(name = "REVIEW_MINI_NO")
	private int reviewMiniNo;

	@Column(name = "DEL_YN")
	private String delYn;
	
	@CreatedDate
	@Column(name = "REVIEW_DT", insertable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
	private String reviewDt;

	@Embedded
    private CommonEntity comonEntity;

}
