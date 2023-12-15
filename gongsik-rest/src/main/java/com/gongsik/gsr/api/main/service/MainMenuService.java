package com.gongsik.gsr.api.main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.main.dto.MainMenuDto;
import com.gongsik.gsr.api.main.entity.MainMenuEntity;
import com.gongsik.gsr.api.main.repository.MainMenuRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainMenuService {
	
	@Autowired
	private MainMenuRepository mainMenuRepository;
	 
	public List<MainMenuDto> menuListAll() {

		 List<MainMenuDto> menuList = mainMenuRepository.findByMenuOrderNo(1);
//		 List<MainMenuDto> menus = new ArrayList<>();
//		 for(MainMenuDto menu : menuList) {
//			 MainMenuDto dto = new MainMenuDto();
//			 dto.setMenuNm(menu.getMenuNm());
//			 dto.setMenuGroupNo(menu.getMenuGroupNo());
//			 dto.setMenuOrderNo(menu.getMenuOrderNo());
//			 dto.setMenuParentNo(menu.getMenuParentNo());
//			 dto.setMenuUrl(menu.getMenuUrl());
//			 menus.add(dto);
//		 }
        return menuList;
        
	}

}
