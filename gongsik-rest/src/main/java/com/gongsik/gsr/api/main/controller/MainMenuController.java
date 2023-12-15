package com.gongsik.gsr.api.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.main.dto.MainMenuDto;
import com.gongsik.gsr.api.main.service.MainMenuService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/main/")	
public class MainMenuController {
	
	@Autowired
	private MainMenuService mainMenuService;
	
	@GetMapping("/menuList")
	public ResponseEntity<List<MainMenuDto>> meneList() {
		List<MainMenuDto> list = mainMenuService.menuListAll();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
//}
//	@GetMapping("/menuList")
//	public Map<String, Object> meneList() {
//		Map<String,Object> map = new HashMap<String, Object>();
//		
//		List<MainMenuDto> list = mainMenuService.menuListAll();
//		
//		map.put("list",  list);
//		map.put("status", "ok");
//		map.put("msg", "전송완료");
//		
//		return map;
//	}
}
