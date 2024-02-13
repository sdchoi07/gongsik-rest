package com.gongsik.gsr.api.mypage.usrGrade.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.mypage.profile.service.ProfileService;
import com.gongsik.gsr.api.mypage.usrGrade.service.UsrGradeService;
import com.gongsik.gsr.global.vo.ResultVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mypage/usrGrade")
@Tag(name = "UsrGradeController", description = "마이페이지 등급")
@RequiredArgsConstructor
public class UsrGradeController {
	@Autowired
	private UsrGradeService usrGradeService;
	
	//회원 정보  
	@PostMapping("/select")
	@Operation(summary = "회원 등급 조회", description = "회원 등급 조회 하기")
	@Parameters({
        @Parameter(description = "사용자아이디", name = "usrId", example = "test"),
        @Parameter(description = "사용자전화번호", name = "usrPhNo", example = "01011111111")
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "회원 등급 조회 성공",
		               content = @Content(
		                    schema = @Schema(implementation = ResultVO.class)))
		})
	public ResponseEntity<Map<String, Object>> usrGradeSelect(@RequestBody Map<String,String> map){
		
		Map<String, Object> resultMap = usrGradeService.usrGradeSelect(map);
		resultMap.put("code", "success");
		System.out.print(resultMap);
		return new ResponseEntity<>(resultMap, HttpStatus.OK); 
	}
}
