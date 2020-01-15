package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.hsrm.mi.swtp.exchangeplatform.model.TimeslotRequestBody;
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


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TimeSlotRestControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@Test
	void testGetAll() throws Exception{
		var token = getLoginToken();
		mockMvc.perform(get("/api/v1/timeslots")
								.header("Authorization", "Bearer " + token))
			   .andExpect(status().isOk());
	}
	
	@Test
	void testGetById() throws Exception {
		var token = getLoginToken();
		var result = mockMvc.perform(get("/api/v1/timeslots/24")
								.header("Authorization", "Bearer " + token))
			   .andExpect(status().isOk())
			   .andReturn().getResponse().getContentAsString();
		assertNotNull("GetById null", result);
	}
	
	// TODO: Nachfragen, was genau der endpunkt tun soll
	//@Test
	void testJoinAppointment() throws Exception {
		var token = getLoginToken();
		TimeslotRequestBody timeslotRequestBody = new TimeslotRequestBody();
		timeslotRequestBody.setStudentId(8L);
		timeslotRequestBody.setTimeslotId(24L);
		var result = mockMvc.perform(post("/api/v1/timeslots/join")
											 .header("Authorization", "Bearer " + token)
									.contentType(MediaType.APPLICATION_JSON)
									.content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
				.andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertNotNull("Join Appointment null", result);
	}
	
	//TODO: wenn anderes todo geklärt: leaveappointment test nachziehen
	
	private String getLoginToken() throws Exception {
		LoginRequestBody json = new LoginRequestBody();
		json.setUsername("dscha001");
		json.setPassword("dscha001");
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
