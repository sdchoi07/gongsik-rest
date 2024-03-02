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
@Table(name = "GS_REPLY_INF")
public class ReplyEntity {
	
	@Id
	@Column(name = "REPLY_SEQ", nullable = false)
	private long replySeq;
	
	@Column(name = "REPLY_NO")
	private int replyNo;
	
	@Column(name = "POST_NO")
	private int postNo;
	
	@Column(name = "REPLY_ID")
	private String replyId;
	
	@Column(name = "REPLY_NM")
	private String replyNm;

	@Column(name = "REPLY_TEXT")
    private String replyText;
	
	@Column(name = "REPLY_MINI_NO")
	private int replyMiniNo;

	@Column(name = "DEL_YN")
	private String delYn;
	
	@CreatedDate
	@Column(name = "REPLY_DT", insertable = false, columnDefinition = "datetime default CURRENT_TIMESTAMP")
	private String replyDt;

	@Embedded
    private CommonEntity comonEntity;

}
