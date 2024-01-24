package com.gongsik.gsr.api.account.join.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	//아이디 중복 조회 
	@GetMapping("/join/emailChk/{usrId}")
	@Operation(summary = "중복 아이디", description = "중복아이디 체크")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<ResultVO> emailChkBtn(@PathVariable("usrId") String usrId){
		Map<String, String>  map = new HashMap<String, String>();
		ResultVO resultVo = new ResultVO();
		//국제번호 list에 담기 
		resultVo  = joinService.selectChkusrId(usrId);
		System.out.println(resultVo);
		return ResponseEntity.ok(resultVo);
	}
	
	//핸드폰 인증 번호 저장 
	@PostMapping("/join/authNoSave")
	@Operation(summary = "인증번호", description = "인증번호 저장")
	@Parameters({
        @Parameter(description = "인증 번호", name = "authNo", example = "1111"),
        @Parameter(description = "사용자아이디", name = "usrId", example = "test"),
        @Parameter(description = "인증타입", name = "authType", example = "J"),
        @Parameter(description = "사용자전화번호", name = "usrPhNo", example = "01011111111")
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "인증번호 요청 성공",
		               content = @Content(
		                    schema = @Schema(implementation = ResultVO.class)))
		})
	public ResponseEntity<ResultVO> authNoSave(@RequestBody JoinDto joinDto){
		
		ResultVO resultVo = new ResultVO();
		//국제번호 list에 담기 
		resultVo = joinService.authNoSave(joinDto);
		
		
		return ResponseEntity.ok(resultVo); 
	}
	
	//회원가입  
	@PostMapping("/join/signUp")
	@Operation(summary = "회원가입", description = "회원가입 하기")
	@Parameters({
        @Parameter(description = "인증 번호", name = "authNo", example = "1111"),
        @Parameter(description = "사용자 생년월일", name = "usrNo", example = "19990101"),
        @Parameter(description = "사용자성별", name = "usrSex", example = "M"),
        @Parameter(description = "사용자비밀번호", name = "usrPwd", example = "12311231"),
        @Parameter(description = "사용자아이디", name = "usrId", example = "test"),
        @Parameter(description = "인증타입", name = "authType", example = "J"),
        @Parameter(description = "사용자전화번호", name = "usrPhNo", example = "01011111111")
        
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "회원가입 성공",
		               content = @Content(
		                    schema = @Schema(implementation = ResultVO.class)))
		})
	public ResponseEntity<ResultVO> join(@RequestBody Map<String,String> map){
		
		ResponseEntity<ResultVO> resultVo = joinService.joinForm(map);
		
		return resultVo; 
	}
	
	@GetMapping("/test")
	public String test() {
		return "성공";
	}

	@GetMapping("/admin")
	public String admin() {
		return"admin";
	}
}
