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
	
	
	 
	public List<MainMenuDto> menuListAll() {
//		MainMenuEntity mainMenuEntity = new MainMenuEntity();
//		em.persist(mainMenuEntity);
//		QMainMenuEntity menuEntitiy1 = QMainMenuEntity.mainMenuEntity;
//		QMainMenuEntity menuEntitiy2 = QMainMenuEntity.mainMenuEntity;
//		JPAQueryFactory query1 = new JPAQueryFactory(em);
//		List<Tuple> result = query1.select(menuEntitiy1.menuNm,menuEntitiy1.menuNo,menuEntitiy1.menuGroupNo)
//			 .from(menuEntitiy1)
//			 .where(menuEntitiy1.menuLevelNo.eq(0))
//			 .union(JPAExpressions.select(menuEntitiy1.menuNm,menuEntitiy1.menuNo,menuEntitiy1.menuGroupNo)
//					 .from(menuEntitiy1)
//					 .join(menuEntitiy2).on(menuEntitiy1.menuNo.eq(menuEntitiy2.menuLevelNo) , menuEntitiy1.menuGroupNo.eq(menuEntitiy2.menuGroupNo)));
//		JPAQueryFactory query2 = new JPAQueryFactory(em);
//		query2.select(menuEntitiy1.menuNm,menuEntitiy1.menuNo,menuEntitiy1.menuGroupNo)
//			 .from(menuEntitiy1)
//			 .join(menuEntitiy2).on(menuEntitiy1.menuNo.eq(menuEntitiy2.menuLevelNo) , menuEntitiy1.menuGroupNo.eq(menuEntitiy2.menuGroupNo));
//			 
//		JPAQueryFactory queryUnion = new JPAQueryFactory(em);
		
			 
			 
			 
		 List<MainMenuEntity> menuList = mainMenuRepository.findAllByOrderByMenuGroupNo();
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
