package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.ModuleService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ModuleRestControllerTest extends BaseRestTest {
	@Autowired
	ModuleRepository moduleRepository;
	
	@Autowired
	ModuleService moduleService;
	
	@Autowired
	UserService userService;
	
	@Test
	void testById() throws Exception {
		var mod = moduleRepository.findAll().get(0);
		var token = getLoginToken("dscha001", "dscha001");
		var result = mockMvc.perform(get("/api/v1/modules/" + mod.getId()).header("Authorization", "Bearer " + token))
							.andExpect(status().isOk())
							.andReturn()
							.getResponse()
							.getContentAsString();
		assertNotNull("GetAll null", result);
	}
	
	@Test
	void testGetByIdUnauthorized() throws Exception {
		var mod = moduleRepository.findAll().get(0);
		mockMvc.perform(get("/api/v1/modules/" + mod.getId())).andExpect(status().isUnauthorized());
	}
	@Test
	@Transactional
	void testGetModulesForStudent() throws Exception{
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var token = getLoginToken("dscha001", "dscha001");
		var result = new ObjectMapper().readValue(mockMvc.perform(get("/api/v1/modules/modulesforstudent/" + usr.getId())
									.header("Authorization", "Bearer "+ token))
											 .andExpect(status().isOk())
											 .andReturn()
											 .getResponse()
											 .getContentAsString(),
												 List.class);
		assertEquals("Equals modules for student", moduleService.lookUpAvailableModulesForStudent(usr), result);
	}
	@Test
	void testUnauthorizedGetModulesForStudent() throws Exception {
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var token = getLoginToken("wwuse001", "wwuse001");
		mockMvc.perform(get("/api/v1/modules/modulesforstudent/"+usr.getId())
					   .header("Authorization", "Bearer " + token))
			   .andExpect(status().isForbidden());
	}
}
