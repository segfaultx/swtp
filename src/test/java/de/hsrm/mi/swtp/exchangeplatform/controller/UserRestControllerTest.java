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


import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
	
}
