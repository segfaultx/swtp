package de.hsrm.mi.swtp.exchangeplatform.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ModuleRestControllerTest extends BaseRestTest {
	
	@Test
	void testById() throws Exception {
		var token = getLoginToken("dscha001", "dscha001");
		var result = mockMvc.perform(get("/api/v1/modules/11")
									.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();
		assertNotNull("GetAll null", result);
	}
	
	@Test
	void testGetByIdUnauthorized() throws Exception {
		mockMvc.perform(get("/api/v1/modules/11"))
			   .andExpect(status().isUnauthorized());
	}
}
