package com.gongsik.gsr.api.mypage.delivery.controller;


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

import com.gongsik.gsr.api.mypage.delivery.service.DeliveryService;

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
@RequestMapping("/api/mypage/delv")
@Tag(name = "Delivery Controller", description = "주문 내역")
@RequiredArgsConstructor
public class DeliveryController {

	@Autowired
	private DeliveryService deliveryService;
	
	@GetMapping("/delvList/{usrId}")
	@Operation(summary = "배송 지역 조회", description = "배송 지역 조회 하기")
	@Parameters({
        @Parameter(description = "사용자 아이디", name = "usrId", example = "test"),
        @Parameter(description = "사용자 이름", name = "usrNm", example = "test@gmail.com"),
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "배송 지역 조회 성공",
		               content = @Content(
		                    schema = @Schema(implementation = Map.class)))
		})
	public ResponseEntity<Map<String, Object>> delvList(@PathVariable("usrId") String usrId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = deliveryService.delvList(usrId);
		return ResponseEntity.ok(map);
	}
	
	@PostMapping("/saveNewAddress")
	@Operation(summary = "새배송 지역 저장", description = "새배송 지역 저장 하기")
	@Parameters({
		@Parameter(description = "사용자 아이디", name = "usrId", example = "test"),
		@Parameter(description = "사용자 이름", name = "usrNm", example = "test@gmail.com"),
	})
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "배송 지역 조회 성공",
					content = @Content(
							schema = @Schema(implementation = Map.class)))
	})
	public ResponseEntity<Map<String, Object>> saveNewAddress(@RequestBody Map<String, String> request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = deliveryService.saveNewAddress(request);
		return ResponseEntity.ok(map);
	}

}
