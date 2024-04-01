package com.gongsik.gsr.api.main.menu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.main.menu.dto.MainMenuDto;
import com.gongsik.gsr.api.main.menu.entity.MainMenuEntity;
import com.gongsik.gsr.api.main.menu.repository.MainMenuRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainMenuService {
	
	private final Logger log =LoggerFactory.getLogger(MainMenuService.class);
	@Autowired
	private MainMenuRepository mainMenuRepository;
	@Autowired
	EntityManager em;
	
	
	 
	public List<MainMenuDto> menuListAll(String usrRole) {
		
			 
			 
		//메뉴 조회	 
		 List<MainMenuEntity> menuList = mainMenuRepository.findAll(usrRole);
		 List<MainMenuDto> menus =  menuList.stream()
										    .map(menu -> {
										        MainMenuDto dto = new MainMenuDto();
										        dto.setMenuNm(menu.getMenuNm());
										        dto.setMenuGroupNo(menu.getMenuGroupNo());
										        dto.setMenuOrderNo(menu.getMenuOrderNo());
										        dto.setMenuLevelNo(menu.getMenuLevelNo());
										        dto.setMenuUrl(menu.getMenuUrl());
										        dto.setMenuItemNo(menu.getMenuItemNo());
										        return dto;
										    })
										    .collect(Collectors.toList());
        return menus;
        
	}

}
