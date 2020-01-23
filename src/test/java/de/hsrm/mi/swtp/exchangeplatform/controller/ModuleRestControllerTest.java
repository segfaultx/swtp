package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ModuleRestControllerTest extends BaseRestTest {
	@Autowired
	ModuleRepository moduleRepository;
	
	@Test
	void testById() throws Exception {
		var mod = moduleRepository.findAll().get(0);
 		var token = getLoginToken("dscha001", "dscha001");
		var result = mockMvc.perform(get("/api/v1/modules/" + mod.getId())
									.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();
		assertNotNull("GetAll null", result);
	}
	
	@Test
	void testGetByIdUnauthorized() throws Exception {
		var mod = moduleRepository.findAll().get(0);
		mockMvc.perform(get("/api/v1/modules/" + mod.getId()))
			   .andExpect(status().isUnauthorized());
	}
}
