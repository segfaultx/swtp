package de.hsrm.mi.swtp.exchangeplatform.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.WhoAmI;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationRestControllerTest extends BaseRestTest {
	
	private String username = "wweit001";
	private String pass = "wweit001";

	@Test
	void testLogin() throws Exception{
		var response = getLoginToken(username, pass);
		assertNotNull("JWT Null",response);
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
		var token = getLoginToken(username, pass);
		mockMvc.perform(post("/api/v1/auth/logout")
					   .header("Authorization","Bearer "+ token))
			   .andExpect(status().isOk());
	}
	
	//TODO: wait for exception handling
	void testFailedLogout() throws Exception {
		var token = getLoginToken(username, pass);
		var failToken = token.replace("a", "e");
		mockMvc.perform(post("/ap1/v1/auth/logout")
									.header("Authorization","Bearer " + failToken))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	void testWhoAmI() throws Exception {
		var token = getLoginToken(username, pass);
		var response = mockMvc.perform(get("/api/v1/auth/whoami")
											   .header("Authorization", "Bearer " + token))
							  .andExpect(status().isOk())
			   .andReturn();
		WhoAmI out = new ObjectMapper().readValue(response.getResponse().getContentAsString(), WhoAmI.class);
		assertEquals("WhoAmI equals USERNAME", out.getUsername(), "wweit001");
		assertEquals("WhoAmI equals ROLE", out.getRole(), Roles.ADMIN);
	}

}
