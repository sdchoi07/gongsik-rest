package com.gongsik.gsr.api.main.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gongsik.gsr.api.main.menu.entity.MainMenuEntity;


@Repository	
public interface MainMenuRepository extends JpaRepository<MainMenuEntity, String>{
	
	//기본 메뉴 조회 쿼리
	@Query(value= "      SELECT						"
			+ "    mme1_0.menu_nm,			"
			+ "    mme1_0.chg_dt,			"
			+ "    mme1_0.chg_id,			"
			+ "    mme1_0.chg_ip,				"
			+ "    mme1_0.crt_dt,				"
			+ "    mme1_0.crt_id,				"
			+ "    mme1_0.crt_ip,				"
			+ "    mme1_0.menu_group_no,				"
			+ "    mme1_0.menu_item_no,					"
			+ "    mme1_0.menu_level_no,					"
			+ "    mme1_0.menu_no,					"
			+ "    mme1_0.menu_order_no,					"
			+ "    mme1_0.menu_url 				"
			+ "	FROM			"
			+ "    gs_menu_mst mme1_0 				"
			+ "  WHERE				"
			+ "    (									"
			+ "        			"
			+ "        'ADMIN' = :usrRole 						"
			+ "        OR (								"
			+ "            				"
			+ "            'USER' = :usrRole 						 "
			+ "            AND MME1_0.MENU_GROUP_NO != '9000' 					"
			+ "        )				"
			+ "        OR(					"
			+ "             MME1_0.MENU_GROUP_NO != '9000' 					"
			+ "        )					"
			+ "    )					"
			+ " ORDER BY					"
			+ "    mme1_0.menu_group_no			" , nativeQuery= true)
   	List<MainMenuEntity> findAll(@Param("usrRole") String usrRole );


}
