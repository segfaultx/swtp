package de.hsrm.mi.swtp.exchangeplatform.controller;


import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TimeTableRestControllerTest extends BaseRestTest {
	
	@Autowired
	UserService userService;
	
	@Test
	void testGetPersonalizedTimeTableForStudent() throws Exception {
		var token = getLoginToken("dscha001", "dscha001");
		var result = mockMvc.perform(get("/api/v1/timetables").header("Authorization", "Bearer " + token))
							.andExpect(status().isOk())
							.andReturn()
							.getResponse()
							.getContentAsString();
		assertNotNull("result null", result);
	}
	
	@Test
	@WithMockUser(roles = "MEMBER", username = "dscha001", password = "dscha001")
	@Transactional
	void testGetSuggestedTimeTableForStudent() throws Exception{
		var usr = userService.getByUsername("dscha001").orElseThrow();
		mockMvc.perform(get("/api/v1/timetables/modulesforstudent/" + usr.getId()))
			   .andExpect(status().isOk());
	}
	
}
