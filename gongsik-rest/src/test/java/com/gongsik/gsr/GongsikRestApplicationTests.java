package com.gongsik.gsr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class GongsikRestApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;
	@Test
	void contextLoads() throws Exception {
			menuListSelect();
		
		
	
	
	}
	
	//메뉴 조회 리스트 
	void menuListSelect() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/main/menuList") // API 엔드포인트 URL
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
	}

}
 