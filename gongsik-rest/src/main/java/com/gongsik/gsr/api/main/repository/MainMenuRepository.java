package com.gongsik.gsr.api.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.main.dto.MainMenuDto;
import com.gongsik.gsr.api.main.entity.MainMenuEntity;


@Repository	
public interface MainMenuRepository extends JpaRepository<MainMenuEntity, String>{
	
	//기본 메뉴 조회 쿼리
	
@Query(value="select " + "        a.menuNm, " + "        a.menuGroupNo, " +
	  "        a.menuOrderNo, " + "        a.menuParentNo, " + "        a.menuUrl "
	  + "    from MainMenuEntity a  " + "    where " +
	  "        menuOrderNo= :menuOrderNo ")
	 
   	List<MainMenuDto> findByMenuOrderNo(@Param("menuOrderNo") int menuOrderNo);


}
