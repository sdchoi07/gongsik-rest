package com.gongsik.gsr.api.mypage.order.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.mypage.order.service.OrderService;
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
@RequestMapping("/api/mypage/order")
@Tag(name = "Order Controller", description = "주문 내역")
@RequiredArgsConstructor
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/orderList")
	@Operation(summary = "주문 내역 조회", description = "주문 내역 조회 하기")
	@Parameters({
        @Parameter(description = "주문자 아이디", name = "usrId", example = "test"),
        @Parameter(description = "주문자 이름", name = "usrNm", example = "test@gmail.com"),
        @Parameter(description = "조회 날짜", name = "orderDt", example = "20240101")
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "주문내역 조회 성공",
		               content = @Content(
		                    schema = @Schema(implementation = Map.class)))
		})
	public ResponseEntity<Map<String, Object>> orderList(@RequestBody Map<String, String> request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = orderService.orderList(request);
		return ResponseEntity.ok(map);
	}

}
