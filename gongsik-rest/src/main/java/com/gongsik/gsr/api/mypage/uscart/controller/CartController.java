package com.gongsik.gsr.api.mypage.uscart.controller;

import java.util.HashMap;
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

import com.gongsik.gsr.api.mypage.uscart.service.CartService;
import com.gongsik.gsr.global.vo.ResultVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mypage/usrCart")
@Tag(name = "CartController", description = "마이페이지 장바구니/좋아요")
public class CartController {

	
	@Autowired
	private CartService cartService;
	
	//회원 정보  
	@PostMapping("/cartList")
	@Operation(summary = "장바구니 조회", description = "장바구니 등급 조회 하기")
	@Parameters({
        @Parameter(description = "사용자아이디", name = "usrId", example = "test"),
	})
	@ApiResponses(value = {
			 @ApiResponse(
		               responseCode = "200",
		               description = "장바구니",
		               content = @Content(
		                    schema = @Schema(implementation = ResultVO.class)))
		})
	public ResponseEntity<Map<String, Object>> wishList(@RequestBody Map<String,String> map){
		
		Map<String, Object> resultMap = cartService.wishList(map);
		resultMap.put("code", "success");
		System.out.print(resultMap);
		return new ResponseEntity<>(resultMap, HttpStatus.OK); 
	}
	
	@GetMapping("/cartDel/{cartNo}/{usrId}")
	@Operation(summary = "장바구니 목록 삭제", description = "장바구니 목록 삭제 하기")
	@Parameters({
		@Parameter(description = "장바구니 번호 ", name = "cartNo", example = "1"),
		@Parameter(description = "유저아이디 ", name = "cartNo", example = "test@gmail.com"),
	})
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "장바구니 목록 삭제 성공",
					content = @Content(
							schema = @Schema(implementation = Map.class)))
	})
	public ResponseEntity<Map<String, Object>> cartDel(@PathVariable("usrId") String usrId ,@PathVariable("cartNo") long cartNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = cartService.cartDel(usrId,cartNo);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/wishAdd/{cartNo}/{usrId}")
	@Operation(summary = "장바구니 목록 추가", description = "장바구니 목록 추가 하기")
	@Parameters({
		@Parameter(description = "장바구니 번호 ", name = "cartNo", example = "1"),
		@Parameter(description = "유저아이디 ", name = "cartNo", example = "test@gmail.com"),
	})
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "장바구니 목록 추가 성공",
					content = @Content(
							schema = @Schema(implementation = Map.class)))
	})
	public ResponseEntity<Map<String, Object>> wishAdd(@PathVariable("usrId") String usrId ,@PathVariable("cartNo") long cartNo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map = cartService.wishAdd(usrId,cartNo);
		return ResponseEntity.ok(map);
	}
}
