package com.gongsik.gsr.api.account.join.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.join.service.JoinService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account/join")
@Tag(name = "Join Controller", description = "회원가입")
@RequiredArgsConstructor
public class JoinController {

	@Autowired
	private JoinService joinService;
	
	@GetMapping("/countryPhList")
	@Operation(summary = "국제번호", description = "국제번호 각 나라 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<List<JoinDto>> countryPhNm(){
			List<JoinDto> list = joinService.countryPhNmList();
		return new ResponseEntity<>(list, HttpStatus.OK); 
	}
	
}
