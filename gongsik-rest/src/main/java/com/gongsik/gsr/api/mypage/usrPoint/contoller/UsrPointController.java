package com.gongsik.gsr.api.mypage.usrPoint.contoller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.mypage.usrPoint.service.UsrPointService;
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
@RequestMapping("/api/mypage/usrPoint")
@Tag(name = "UsrPoint Controller", description = "마이페이지 포인트")
@RequiredArgsConstructor
public class UsrPointController {
	
	@Autowired
	private UsrPointService usrPointService;
	
	//회원 정보  
	@PostMapping("/pointList")
	@Operation(summary = "포인트 조회", description = "프로필 조회 하기")
	@Parameters({
        @Parameter(description = "사용자아이디", name = "usrId", example = "test"),
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "프로필 조회 성공",
		               content = @Content(
		                    schema = @Schema(implementation = ResultVO.class)))
		})
	public ResponseEntity<Map<String, Object>> pointList(@RequestBody Map<String,Object> map){
		
		Map<String, Object> resultMap = usrPointService.pointList(map);
		resultMap.put("code", "success");
		return new ResponseEntity<>(resultMap, HttpStatus.OK); 
	}
}
