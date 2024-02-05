package com.gongsik.gsr.api.mypage.profile.contoller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.account.join.service.JoinService;
import com.gongsik.gsr.api.mypage.profile.service.ProfileService;
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
@RequestMapping("/api/mypage/profile")
@Tag(name = "Profile Controller", description = "마이페이지 프로필")
@RequiredArgsConstructor
public class ProfileController {
	
	@Autowired
	private ProfileService profileService;
	
	//회원 정보  
	@PostMapping("/list")
	@Operation(summary = "프로필 조회", description = "프로필 조회 하기")
	@Parameters({
        @Parameter(description = "사용자아이디", name = "usrId", example = "test"),
        @Parameter(description = "사용자전화번호", name = "usrPhNo", example = "01011111111")
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "프로필 조회 성공",
		               content = @Content(
		                    schema = @Schema(implementation = ResultVO.class)))
		})
	public ResponseEntity<Map<String, Object>> profileList(@RequestBody Map<String,String> map){
		
		Map<String, Object> resultMap = profileService.profileList(map);
		resultMap.put("code", "success");
		System.out.println("map : " + resultMap);
		return new ResponseEntity<>(resultMap, HttpStatus.OK); 
	}
			
	
	// 회원가입
	@PostMapping("/modify")
	@Operation(summary = "프로필 수정", description = "프로필 수정 하기")
	@Parameters({ @Parameter(description = "인증 번호", name = "authNo", example = "1111"),
			@Parameter(description = "사용자 생년월일", name = "usrNo", example = "19990101"),
			@Parameter(description = "사용자성별", name = "usrSex", example = "M"),
			@Parameter(description = "사용자비밀번호", name = "usrPwd", example = "12311231"),
			@Parameter(description = "사용자아이디", name = "usrId", example = "test"),
			@Parameter(description = "인증타입", name = "authType", example = "J"),
			@Parameter(description = "사용자전화번호", name = "usrPhNo", example = "01011111111")

	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "프로필 수정 성공", content = @Content(schema = @Schema(implementation = ResultVO.class))) })
	public ResponseEntity<ResultVO> modify(@RequestBody Map<String, String> map) {

		ResponseEntity<ResultVO> resultVo = profileService.accountModify(map);

		return resultVo;
	}
}
