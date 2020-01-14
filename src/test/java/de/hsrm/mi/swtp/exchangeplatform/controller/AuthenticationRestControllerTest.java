package de.hsrm.mi.swtp.exchangeplatform.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationRestControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Test
	void testLogin() throws Exception{
		LoginRequestBody json = new LoginRequestBody();
		json.setPassword("wweit001");
		json.setUsername("wweit001");
		var result = mockMvc.perform(post("/api/v1/auth/login")
											 .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(json)))
							.andExpect(status().isOk()).andReturn();
		assertNotNull("JWT Null",result.getResponse().getContentAsString());
	}
	
	@Test
	void testLoginWithUnkownUser() throws Exception {
		LoginRequestBody falseJson = new LoginRequestBody();
		falseJson.setPassword("wasd");
		falseJson.setUsername("wasd");
		mockMvc.perform(post("/api/v1/auth/login")
									.contentType(MediaType.APPLICATION_JSON)
									.content(new ObjectMapper().writeValueAsString(falseJson)))
				.andExpect(status().isUnauthorized()).andReturn();
	}
	
	@Test
	void testLogout() throws Exception {
		LoginRequestBody json = new LoginRequestBody();
		json.setUsername("wweit001");
		json.setPassword("wweit001");
		var result = mockMvc.perform(post("/api/v1/auth/login")
									.contentType(MediaType.APPLICATION_JSON)
									.content(new ObjectMapper().writeValueAsString(json)))
				.andExpect(status().isOk())
							.andReturn()
							.getResponse()
							.getContentAsString();
		var token = JsonPath.read(result, "$.tokenResponse.token");
		
		mockMvc.perform(post("/api/v1/auth/logout")
					   .header("Authorization","Bearer "+ token))
			   .andExpect(status().isOk());
	}
	
	//TODO: wait for exception handling
	void testFailedLogout() throws Exception {
		LoginRequestBody json = new LoginRequestBody();
		json.setUsername("wweit001");
		json.setPassword("wweit001");
		var result = mockMvc.perform(post("/api/v1/auth/login")
											 .contentType(MediaType.APPLICATION_JSON)
											 .content(new ObjectMapper().writeValueAsString(json)))
							.andExpect(status().isOk())
							.andReturn()
							.getResponse()
							.getContentAsString();
		var token = JsonPath.read(result, "$.tokenResponse.token");
		var failToken = ((String) token).replace("a", "e");
		mockMvc.perform(post("/ap1/v1/auth/logout")
									.header("Authorization","Bearer " + failToken))
				.andExpect(status().isBadRequest());
	}
}
