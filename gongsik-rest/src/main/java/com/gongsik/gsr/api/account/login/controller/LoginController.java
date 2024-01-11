package com.gongsik.gsr.api.account.login.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.login.service.LoginService;
import com.gongsik.gsr.global.vo.ResultVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Login Controller", description = "로그인")
@RequiredArgsConstructor
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	
//	//로그인 
//	@GetMapping("/login")
//	@Operation(summary = "로그인", description = "로그인 하기")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "성공")
//		})
//	public ResponseEntity<ResultVO> login(@RequestBody Map<String,String> map){
//		ResultVO resultVo = new ResultVO();
//		//국제번호 list에 담기 
//		//resultVo = loginService.login();
//		return new ResponseEntity<>(resultVo, HttpStatus.OK); 
//	}
		
	
}
