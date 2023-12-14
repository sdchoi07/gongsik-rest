package com.gongsik.gsr.api.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.main.entity.MainMenuEntity;

@Repository
public interface MainMenuRepository extends JpaRepository<MainMenuEntity, String>{
	
	@Query(
			"   SELECT a.MENU_NM"
		   +"         ,a.MENU_GROUP_NO"
		   +"         ,a.MENU_NO "
		   +"     FROM GS_MENU_MST a"
		   +"    WHERE a.MENU_ORDER_NO = :menuOrderNo")
	List<MainMenuEntity> findByMenuOrderNo(int menuOrderNo);


}
