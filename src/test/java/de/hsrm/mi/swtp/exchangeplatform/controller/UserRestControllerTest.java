package de.hsrm.mi.swtp.exchangeplatform.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserRestControllerTest extends BaseRestTest {
	
	@Autowired
	MockMvc mockMvc;
	
	
	@Test
	void testGetById() throws Exception {
		var token = getLoginToken("dscha001", "dscha001");
		
		var result = mockMvc.perform(get("/api/v1/users/8")
									.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();
		assertNotNull("GetById null",  result);
	}
	@Test
	void testGetByIdUnauthorized() throws Exception {
		mockMvc.perform(get("/api/v1/users/8"))
							.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetAll() throws Exception {
		var token = getLoginToken("wweit001", "wweit001");
		
		var result = mockMvc.perform(get("/api/v1/users")
									.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse().getContentAsString();
		assertNotNull("Get all null", result);
	}
	
	@Test
	void testGetAllUnauthorized() throws Exception {
		var token = getLoginToken("dscha001", "dscha001");
		
		mockMvc.perform(get("/api/v1/users")
					   .header("Authorization", "Bearer " + token))
			   .andExpect(status().isForbidden());
	}
	
	//TODO: fix im usercontroller hinzufügen -> user muss erst aus timeslots ausgetragen
	// werden bevor er gelöscht werden kann, sonst fk constraint violation
	//@Test
	void testDeleteUser () throws Exception {
		var token = getLoginToken("wweit001", "wweit001");
		var result = mockMvc.perform(delete("/api/v1/users/admin/8")
					   .header("Authorization", "Bearer " + token))
			   .andExpect(status().isOk())
			   .andReturn()
			   .getResponse().getContentAsString();
		assertNotNull("Delete user null", result);
	}
	@Test
	void testDeleteUserUnauthorized() throws Exception {
		mockMvc.perform(delete("/api/v1/users/admin/8"))
			   .andExpect(status().isUnauthorized());
	}
	@Test
	void testDeleteuserForbidden() throws Exception {
		var token = getLoginToken("dscha001", "dscha001");
		mockMvc.perform(delete("/api/v1/users/admin/8")
					   .header("Authorization", "Bearer " +token))
			   .andExpect(status().isForbidden());
	}
	
}
