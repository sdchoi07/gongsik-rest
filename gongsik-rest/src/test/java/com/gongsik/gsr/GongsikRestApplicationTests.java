package com.gongsik.gsr;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongsik.gsr.api.account.join.dto.JoinDto;
import com.gongsik.gsr.api.account.join.entity.AuthSMSEntity;
import com.gongsik.gsr.api.account.join.repository.AuthSMSRepository;
@SpringBootTest
@AutoConfigureMockMvc
class GongsikRestApplicationTests {
	
	@Autowired
	private AuthSMSRepository au;
	
	@Autowired
	private MockMvc mockMvc;
	@Test
	void contextLoads() throws Exception {
			//menuListSelect();
			//joinCountryPh();
		     // authoNoSave();
		
	
	
	}
	
	//메뉴 조회 리스트 
	void menuListSelect() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/main/menuList") // API 엔드포인트 URL
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
	}
	
	//국제 번호 조회 
		void joinCountryPh() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.get("/api/account/join/countryPhList") // API 엔드포인트 URL
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
	                .andReturn();
//			assertDoesNotThrow(() ->
//		            userRepository.findUserNumByDivisionJpql());
//		    System.out.println("JPQL 정상");
//		    listJpql = userRepository.findUserNumByDivisionJpql();
//		    assertNotNull(listJpql);
		}
		
		//인증번호 저장 
		void authoNoSave() throws Exception {
			JoinDto dto = new JoinDto();
			dto.setAuthId("test");
			dto.setAuthNo("1111");
			dto.setAuthType("I");
			dto.setAuthYn("Y");
			dto.setSmsSeq(0);
			dto.setUsrId("dj");
			dto.setUsrPhNo("0101111111");
			
			// ObjectMapper를 사용하여 JoinDto 객체를 JSON 문자열로 변환
			ObjectMapper objectMapper = new ObjectMapper();
			String requestBody = objectMapper.writeValueAsString(dto);
			
			
			MvcResult result =mockMvc.perform(MockMvcRequestBuilders.post("/api/account/join/authNoSave") // API 엔드포인트 URL
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(requestBody))
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
	                .andReturn();
		
			 String responseBody = result.getResponse().getContentAsString();


		        
		        // 데이터베이스에 새로운 데이터 삽입
		        Optional<AuthSMSEntity> savedData = au.findByUsrPhNo(dto.getUsrPhNo()); 
			

		        // 삽입한 데이터의 ID가 null이 아닌지 확인하는 예제 Assertion
		        assertThat(responseBody).isEqualTo(savedData);
		}


}
 