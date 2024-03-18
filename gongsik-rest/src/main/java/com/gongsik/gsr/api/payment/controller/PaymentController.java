package com.gongsik.gsr.api.payment.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.payment.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;

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
@RequestMapping("/api/payment")
@Tag(name = "Payment Controller", description = "결제")
@RequiredArgsConstructor
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@GetMapping("/accountInfo/{usrId}")
	@Operation(summary = "결제자 정보 조회", description = "결제자 정보 조회 하기")
	public ResponseEntity<Map<String, Object>> accountInfo(@PathVariable("usrId") String usrId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = paymentService.accountInfo(usrId);
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/verify")
	@Operation(summary = "결제 검증", description = "결제 검증 하기")
	@Parameters({ @Parameter(description = "", name = "", example = ""),
			@Parameter(description = "", name = "", example = ""), })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "결제자 검증  성공", content = @Content(schema = @Schema(implementation = Map.class))) })
	public ResponseEntity<Map<String, Object>> verify(@RequestBody Map<String,Object> request) throws IamportResponseException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = paymentService.verify(request);
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/cancel")
	@Operation(summary = "결제 취소", description = "결제 취소 하기")
	@Parameters({ @Parameter(description = "", name = "", example = ""),
		@Parameter(description = "", name = "", example = ""), })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "결제자 취소 성공", content = @Content(schema = @Schema(implementation = Map.class))) })
	public ResponseEntity<Map<String, Object>> cancel(@RequestBody Map<String,Object> request) throws IamportResponseException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map = paymentService.cancel(request);
		return ResponseEntity.ok(map);
	}

}
