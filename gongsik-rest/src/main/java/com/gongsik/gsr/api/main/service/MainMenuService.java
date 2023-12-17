package com.gongsik.gsr.api.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.main.dto.MainMenuDto;
import com.gongsik.gsr.api.main.entity.MainMenuEntity;
import com.gongsik.gsr.api.main.repository.MainMenuRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainMenuService {
	
	private final Logger log =LoggerFactory.getLogger(MainMenuService.class);
	@Autowired
	private MainMenuRepository mainMenuRepository;
	 
	public List<MainMenuDto> menuListAll() {
		 List<MainMenuEntity> menuList = mainMenuRepository.findByMenuParentNo("0");
		 List<MainMenuDto> menus =  menuList.stream()
										    .map(menu -> {
										        MainMenuDto dto = new MainMenuDto();
										        dto.setMenuNm(menu.getMenuNm());
										        dto.setMenuGroupNo(menu.getMenuGroupNo());
										        dto.setMenuOrderNo(menu.getMenuOrderNo());
										        dto.setMenuParentNo(menu.getMenuParentNo());
										        dto.setMenuUrl(menu.getMenuUrl());
										        return dto;
										    })
										    .collect(Collectors.toList());
        return menus;
        
	}

}
