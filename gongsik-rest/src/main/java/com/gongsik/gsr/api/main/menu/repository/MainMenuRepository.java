package com.gongsik.gsr.api.main.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.main.menu.dto.MainMenuDto;
import com.gongsik.gsr.api.main.menu.entity.MainMenuEntity;


@Repository	
public interface MainMenuRepository extends JpaRepository<MainMenuEntity, String>{
	
	//기본 메뉴 조회 쿼리
   	List<MainMenuEntity> findAllByOrderByMenuGroupNo( );


}
