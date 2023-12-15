package com.gongsik.gsr;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
class GongsikRestApplicationTests {
	
	@Autowired
	private MockMvc mvc;
	@Test
	void contextLoads() throws Exception {
		
			mvc.perform(get("/main")).andDo(print());
	}

}
 