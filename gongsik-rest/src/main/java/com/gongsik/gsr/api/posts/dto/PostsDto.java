package com.gongsik.gsr.api.posts.dto;

import com.gongsik.gsr.api.common.dto.CommonDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "PostDto")
public class PostsDto extends CommonDto{
	
	/* posts */
	private long postsSeq;
	private String postsUsrId;
	private String postsUsrNm;
	private String postsNm;
	private int postsNo;
    private String postsUrl;
	private String postsText;
	private String postsGubun;
	private String postsGubunNm;
	private String postsViewCnt;
	private String postsDt;
	private String postsYMD;
	private String postsTime;
	private String postsModDt;
	private String delYn;
	
	/* review */
	private int ReplySeq;
	private int ReplyNo;
	private int postNo;
	private String replyId;
	private String replyNm;
    private String replyText;
	private int replyMiniNo;
	private String replyDt;
	private String replyYMD;
	private String replyTime;

}
