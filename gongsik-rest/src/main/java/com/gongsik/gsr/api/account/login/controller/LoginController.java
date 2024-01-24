package com.gongsik.gsr.api.account.login.controller;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	
	 
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
//	//로그인 
//	@GetMapping("/login")
//	@Operation(summary = "로그인", description = "로그인 하기")
//	@ApiResponses(value = {
//			@ApiResponse(responseCode = "200", description = "성공")
//		})
//	public ResponseEntity<TokenInfo> login(@RequestBody Map<String,String> map){
//		TokenInfo tokenInfo = new TokenInfo();
//		String usrId = map.get("usrId");
//		String usrPwd = map.get("usrPwd");
//		
//		UsernamePasswordAuthenticationToken authenticationToken = 
//				new UsernamePasswordAuthenticationToken(usrId, usrPwd);
//		Authentication authentication = 
//				authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//		String jwt = jwtTokenProvider.generateToken(authentication);
//		tokenInfo.setAccessToken(jwt);
//		tokenInfo.setUsrNm("test");
////		tokenInfo = loginService.loginSerivce(usrId, usrPwd);
//		//국제번호 list에 담기 
//		//resultVo = loginService.login();
//		return new ResponseEntity<>(tokenInfo, HttpStatus.OK); 
//	}
		
	//유저 데이터
	@PostMapping("/data")
	@Operation(summary = "유저 데이터", description = "유저 데이터 확인")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<ResultVO> loginData(@RequestBody Map<String,String> map){
		ResultVO resultVo = new ResultVO();
		Date now = new Date(System.currentTimeMillis()+ (600000*10));
		String usrId = map.get("usrId");
		String refreshToken = map.get("refreshToken");
        redisTemplate.opsForValue().set("usrId", usrId);
        redisTemplate.opsForValue().set("refreshToken", refreshToken, Duration.ofMillis(now.getTime()));
        loginService.accountData(usrId, refreshToken);
		return new ResponseEntity<>(resultVo, HttpStatus.OK); 
	}
	
	//로그아웃
	@PostMapping("/logout")
	@Operation(summary = "로그아웃", description = "로그인 아웃")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public void logout(@RequestBody Map<String,String> map){
		loginService.logout(map);
	}
	
}
