package com.gongsik.gsr.api.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.admin.service.AdminService;
import com.gongsik.gsr.api.chat.service.ChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Controller", description = "운영")
@RequiredArgsConstructor
public class AdminController {
	
	@Autowired
	private AdminService adminService;

	@GetMapping("/itemList")
	@Operation(summary = "아이템 목록 조회", description = "아이템 목록 조회 하기")
	public ResponseEntity<Map<String, Object>> itemList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map = adminService.itemList();
		return ResponseEntity.ok(map);
	}
}
