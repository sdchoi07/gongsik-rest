package com.gongsik.gsr.api.main.categories.dto;

import com.gongsik.gsr.api.common.dto.CommonDto;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Schema(description = "CategoriesDto")
public class CategoriesDto extends CommonDto{

	/* INVEN MST */
	private long invenSeq;
    private String invenLClsNm;
    private String invenMClsNm;
    private String invenSClsNm;
    private String invenLClsNo;
    private String invenMClsNo;
    private String invenSClsNo;
    private String invenPrice;
    private int invenSaelsCnt;
    private int invenCnt;
    private String crgDate;
    private String endDate;
    private String delYn;
    private String useYn;
	private String invenUrl;
    
	
    @QueryProjection
	  public CategoriesDto(String invenSClsNm, String invenSClsNo, String invenPrice, int invenSaelsCnt, int invenCnt, String invenUrl, String delYn, String useYn) {
	    this.invenSClsNm = invenSClsNm;
	    this.invenSClsNo = invenSClsNo;
	    this.invenPrice = invenPrice;
	    this.invenSaelsCnt = invenSaelsCnt;
	    this.invenCnt = invenCnt;
	    this.invenUrl = invenUrl;
	    this.delYn = delYn;
	    this.useYn = useYn;
	  }


}
