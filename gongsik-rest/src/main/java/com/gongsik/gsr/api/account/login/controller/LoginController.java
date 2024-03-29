package com.gongsik.gsr.api.account.login.controller;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.account.login.service.LoginService;
import com.gongsik.gsr.global.jwt.JwtProvider;
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
	// 로그인
//	@PostMapping("/login")
//	@Operation(summary = "로그인", description = "로그인 하기")
//	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공") })
//	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> map) {
//		Map<String, Object> result = new HashMap<String, Object>();
//
//
//		boolean tokenCheck = loginService.tokenChk(map);
//		if (tokenCheck) {
//
//			result.put("code", "01");
//		} else {
//			
//			String usrId = map.get("usrId");
//			String usrPwd = map.get("usrPwd");
//			Authentication authentication = jwtProvider.getAuthentication(usrId, usrPwd);
//			result = loginService.accountData(authentication);
//			result.put("code", "02");
//		}
//
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}

	// 유저 데이터
	@PostMapping("/data")
	@Operation(summary = "유저 데이터", description = "유저 데이터 확인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<Map<String,Object>> loginData(@RequestBody Map<String,String> map){
		Map<String,Object> result = new HashMap<String, Object>();
		String usrId = map.get("usrId");
        result = loginService.accountData(usrId);
        
		return new ResponseEntity<>(result, HttpStatus.OK); 
	}

	// SNS로그인
	@PostMapping("/login/OAuth")
	@Operation(summary = "sns 로그인", description = "sns 로그인")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공") })
	public ResponseEntity<ResultVO> SNSLogin(@RequestBody Map<String, String> map) {
		ResultVO resultVo = new ResultVO();

		resultVo = loginService.SNSLogin(map);
		return new ResponseEntity<>(resultVo, HttpStatus.OK);
	}

	// 로그아웃
	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "로그인 아웃")
	public void logout(@RequestBody Map<String, String> map) {
		loginService.logout(map);
	}

}
