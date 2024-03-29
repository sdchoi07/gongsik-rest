package com.gongsik.gsr.api.main.categories.controller;

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

import com.gongsik.gsr.api.main.categories.service.CategoriesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")	
@Tag(name = "Categoreis Controller", description = "카테고리")
public class CategoreisController {
	
	@Autowired
	private CategoriesService categoriesService;
	
	@PostMapping("/categoriesList")
	@Operation(summary = "카테고리 조회", description = "카테고리 메뉴 조회")
	@Parameters({
        @Parameter(description = "상품 고유 번호", name = "itemkey", example = "110000"),
        @Parameter(description = "사용자 아이디", name = "usrId", example = "test@gmail.com"),
        @Parameter(description = "상품 등록 날짜", name = "crgDate", example = "20240303"),
        @Parameter(description = "상품 만료 날짜", name = "endDate", example = "99991231")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<Map<String,Object>> meneList(@RequestBody Map<String,Object> request) {
		Map<String, Object> map = new HashMap<>();
		map = categoriesService.categoriesListAll(request);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	
	@PostMapping("/intoCart")
	@Operation(summary = "찜하기", description = "찜하기")
	@Parameters({
        @Parameter(description = "상품 고유 번호", name = "invenNo", example = "111000"),
        @Parameter(description = "사용자 아이디", name = "usrId", example = "test@gmail.com"),
        @Parameter(description = "장바구니 종류", name = "cartSt", example = "A"),
        @Parameter(description = "장바구니 사용", name = "useYn", example = "Y"),
        @Parameter(description = "장바구니 삭제", name = "delYn", example = "N")
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<Map<String,Object>> intoCart(@RequestBody Map<String,Object> request) {
		Map<String, Object> map = new HashMap<>();
		map = categoriesService.intoCart(request);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@PostMapping("/categorieDetail")
	@Operation(summary = "상품 디테일 정보", description = "상품 디테일 정보")
	@Parameters({
        @Parameter(description = "상품 대분류 번호", name = "itemKey", example = "111000"),
        @Parameter(description = "사용자 아이디", name = "usrId", example = "test@gmail.com"),
	})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<Map<String,Object>> categorieDetail(@RequestBody Map<String,Object> request) {
		Map<String, Object> map = new HashMap<>();
		map = categoriesService.categorieDetail(request);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	

}
