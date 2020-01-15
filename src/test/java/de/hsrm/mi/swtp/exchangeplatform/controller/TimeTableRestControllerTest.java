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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TimeTableRestControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	void testGetPersonalizedTimeTableForStudent() throws Exception {
		var token = getLoginToken();
		var result = mockMvc.perform(get("/api/v1/timetables")
					   .header("Authorization", "Bearer " + token))
			   .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertNotNull("result null", result);
	}
	
	
	private String getLoginToken() throws Exception {
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
		return JsonPath.read(result, "$.tokenResponse.token");
	}
}
