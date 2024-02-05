package com.gongsik.gsr.api.main.menu.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gongsik.gsr.api.main.menu.dto.MainMenuDto;
import com.gongsik.gsr.api.main.menu.service.MainMenuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/main")	
//@CrossOrigin(origins = "http://localhost:8080")
@Tag(name = "Menu Controller", description = "메뉴정보")
public class MainMenuController {
	
	private final Logger log =LoggerFactory.getLogger(MainMenuController.class);
	
	@Autowired
	private MainMenuService mainMenuService;
	
	@GetMapping("/menuList")
	@Operation(summary = "메뉴 조회", description = "메인 화면의 메뉴 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity<List<MainMenuDto>> meneList() {
		List<MainMenuDto> list = mainMenuService.menuListAll();
		log.info("menuList:{}", list);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/loginInOut")
	@Operation(summary = "로그인 유무", description = "메인 화면의 메뉴 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
		})
	public ResponseEntity loginInOut() {
		 boolean validationCondition = true; // 예시 조건

		    if (validationCondition) {
		        return new ResponseEntity(HttpStatus.OK);
		    } else {
		        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/account/login").build();
		    }
	}
	
	
	@GetMapping("/chk")
	@Operation(summary = "검증 체크", description = "검증 체크")
	public ResponseEntity chk() {
		
		 boolean validationCondition = true; // 예시 조건

	    if (validationCondition) {
	        return new ResponseEntity(HttpStatus.OK);
	    } else {
	        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/account/login").build();
	    }
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
