package com.gongsik.gsr.api.main.categories.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.main.categories.service.CategoriesService;

import io.swagger.v3.oas.annotations.Operation;
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
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<Map<String,Object>> intoCart(@RequestBody Map<String,Object> request) {
		Map<String, Object> map = new HashMap<>();
		map = categoriesService.intoCart(request);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
