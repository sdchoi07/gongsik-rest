package com.gongsik.gsr.api.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gongsik.gsr.api.admin.dto.ItemListDto;
import com.gongsik.gsr.api.main.categories.repository.CategoriesRepository;

@Service
public class AdminService {
	
	@Autowired
	private CategoriesRepository categoriesRepository;
	
	public Map<String, Object> itemList() {
		Map<String, Object> map = new HashMap<>();
		List<ItemListDto> entity = categoriesRepository.findByEndDate("99991231");
		map.put("size", entity.size());
		map.put("list", entity);
		return map;
	}

}
