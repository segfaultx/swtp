package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TimeslotRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TimeSlotRestControllerTest extends BaseRestTest {
	
	String username = "dscha001";
	String pass = "dscha001";
	
	@Autowired
	TimeslotRepository timeslotRepository;
	
	@Test
	void testGetById() throws Exception {
		var ts = timeslotRepository.findAll().get(0);
		var token = getLoginToken(username, pass);
		var result = mockMvc.perform(get("/api/v1/timeslots/" + ts.getId()).header("Authorization", "Bearer " + token))
							.andExpect(status().isOk())
							.andReturn()
							.getResponse()
							.getContentAsString();
		assertNotNull("GetById null", result);
	}
	
	// TODO: Nachfragen, was genau der endpunkt tun soll
	//@Test
	void testJoinAppointment() throws Exception {
		var token = getLoginToken(username, pass);
		TimeslotRequestBody timeslotRequestBody = new TimeslotRequestBody();
		timeslotRequestBody.setStudentId(8L);
		timeslotRequestBody.setTimeslotId(24L);
		var result = mockMvc.perform(post("/api/v1/timeslots/join").header("Authorization", "Bearer " + token)
																   .contentType(MediaType.APPLICATION_JSON)
																   .content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
							.andExpect(status().isOk())
							.andReturn()
							.getResponse()
							.getContentAsString();
		assertNotNull("Join Appointment null", result);
	}
	
	//TODO: wenn anderes todo geklärt: leaveappointment test nachziehen
	
}
