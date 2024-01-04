package com.gongsik.gsr.api.account.join.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.join.service.JoinService;
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
@RequestMapping("/api/account")
@Tag(name = "Join Controller", description = "회원가입")
@RequiredArgsConstructor
public class JoinController {
	
	
	@Autowired
	private JoinService joinService;
	
	//국제번호 조회 
	@GetMapping("/join/countryPhList")
	@Operation(summary = "국제번호", description = "국제번호 각 나라 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<List<JoinDto>> countryPhNm(){
		//국제번호 list에 담기 
		List<JoinDto> list = joinService.countryPhNmList();
		return new ResponseEntity<>(list, HttpStatus.OK); 
	}
	
	//핸드폰 인증 번호 저장 
	@PostMapping("/join/authNoSave")
	@Operation(summary = "인증번호", description = "인증번호 저장")
	@Parameters({
        @Parameter(description = "인증 번호", name = "authNo", example = "1111"),
        @Parameter(description = "사용자아이디", name = "usrId", example = "test"),
        @Parameter(description = "사용자아이디", name = "authId", example = "test"),
        @Parameter(description = "사용자아이디", name = "authType", example = "I"),
        @Parameter(description = "사용자아이디", name = "usrPhNo", example = "01011111111")
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "인증번호 요청 성공",
		               content = @Content(
		                    schema = @Schema(implementation = ResultVO.class)))
		})
	public Map<String, Object> authNoSave(@RequestBody JoinDto joinDto){
		System.out.println("cnt : " + joinDto );
		Map<String, Object> map = new HashMap<String, Object>();
		
		ResultVO resultVo = new ResultVO();
		//국제번호 list에 담기 
		resultVo = joinService.authNoSave(joinDto);
		map.put("code", resultVo.getErrCode());
		map.put("msg", resultVo.getErrMsg());
		
		
		return map; 
	}
	
}
