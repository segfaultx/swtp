package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserRestControllerTest extends BaseRestTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@Test
	void testGetById() throws Exception {
		var dennis = userRepository.findByUsername("dscha001").orElseThrow();
		var token = getLoginToken("dscha001", "dscha001");
		var result = mockMvc.perform(get("/api/v1/users/" + dennis.getId()).header("Authorization", "Bearer " + token))
							.andExpect(status().isOk())
							.andReturn()
							.getResponse()
							.getContentAsString();
		assertNotNull("GetById null", result);
	}
	
	@Test
	void testGetByIdUnauthorized() throws Exception {
		mockMvc.perform(get("/api/v1/users/8")).andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetAll() throws Exception {
		var token = getLoginToken("wweit001", "wweit001");
		
		var result = mockMvc.perform(get("/api/v1/users").header("Authorization", "Bearer " + token))
							.andExpect(status().isOk())
							.andReturn()
							.getResponse()
							.getContentAsString();
		assertNotNull("Get all null", result);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN", username = "wweit001", password = "wweit001")
	void testGetByusernameQuery() throws Exception {
		var usr  = userService.getByUsername("dscha001").orElseThrow();
		var response = Long.valueOf(JsonPath.read(mockMvc.perform(get("/api/v1/users?username=dscha001"))
									   .andExpect(status().isOk())
									   .andReturn()
									   .getResponse()
									   .getContentAsString(), "$.id").toString());
		assertEquals("User by query equals", usr.getId(), response);
		
	}
	
	@Test
	void testGetAllUnauthorized() throws Exception {
		var token = getLoginToken("dscha001", "dscha001");
		
		mockMvc.perform(get("/api/v1/users").header("Authorization", "Bearer " + token)).andExpect(status().isForbidden());
	}
	
}
