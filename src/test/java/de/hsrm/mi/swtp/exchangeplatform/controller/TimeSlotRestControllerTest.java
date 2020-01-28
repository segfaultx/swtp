package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TimeslotRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeslotService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TimeSlotRestControllerTest extends BaseRestTest {
	
	String username = "dscha001";
	String pass = "dscha001";
	
	@Autowired
	TimeslotRepository timeslotRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TimeslotService timeslotService;
	
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
	
	@Test
	@WithMockUser(roles = "MEMBER")
	@Transactional
	void testJoinAppointment() throws Exception {
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var timeslot = usr.getTimeslots().get(0);
		timeslotService.removeAttendeeFromTimeslot(timeslot, usr);
		TimeslotRequestBody timeslotRequestBody = new TimeslotRequestBody();
		timeslotRequestBody.setStudentId(usr.getId());
		timeslotRequestBody.setTimeslotId(timeslot.getId());
		mockMvc.perform(post("/api/v1/timeslots/join").contentType(MediaType.APPLICATION_JSON)
																   .content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
							.andExpect(status().isOk());
		assertTrue("User joined appointment", userService.getByUsername("dscha001").orElseThrow().getTimeslots().contains(timeslot));
	}
	
	@Test
	@WithMockUser(roles = "MEMBER")
	@Transactional
	void testJoinAppointmentUserIsAlreadyAttendee() throws Exception {
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var timeslot = usr.getTimeslots().get(0);
		TimeslotRequestBody timeslotRequestBody = new TimeslotRequestBody();
		timeslotRequestBody.setStudentId(usr.getId());
		timeslotRequestBody.setTimeslotId(timeslot.getId());
		mockMvc.perform(post("/api/v1/timeslots/join").contentType(MediaType.APPLICATION_JSON)
																   .content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
							.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(roles = "MEMBER")
	void testJoinAppointmentBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/timeslots/join"))
			   .andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(roles = "MEMBER")
	void testLeaveAppointmentBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/timeslots/leave"))
			   .andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(roles = "MEMBER")
	@Transactional
	void testLeaveAppointment() throws Exception {
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var timeslot = usr.getTimeslots().get(0);
		TimeslotRequestBody timeslotRequestBody = new TimeslotRequestBody();
		timeslotRequestBody.setStudentId(usr.getId());
		timeslotRequestBody.setTimeslotId(timeslot.getId());
		mockMvc.perform(post("/api/v1/timeslots/leave").contentType(MediaType.APPLICATION_JSON)
																   .content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
							.andExpect(status().isOk());
		assertTrue("User left appointment", !timeslotService.getById(timeslot.getId()).orElseThrow().getAttendees().contains(usr));
	}
	@Test
	@WithMockUser(roles = "MEMBER")
	@Transactional
	void testAddToWaitlist() throws Exception {
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var timeslot = usr.getTimeslots().get(0);
		timeslotService.removeAttendeeFromTimeslot(timeslot, usr);
		TimeslotRequestBody timeslotRequestBody = new TimeslotRequestBody();
		timeslotRequestBody.setStudentId(usr.getId());
		timeslotRequestBody.setTimeslotId(timeslot.getId());
		mockMvc.perform(post("/api/v1/timeslots/addToWaitlist").contentType(MediaType.APPLICATION_JSON)
																	.content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
							.andExpect(status().isOk());
		assertTrue("User in waitlist", timeslotService.getById(timeslot.getId()).orElseThrow().getWaitList().contains(usr));
	}
	@Test
	@WithMockUser(roles = "MEMBER")
	@Transactional
	void testAddToWaitlistAlreadyAttendee() throws Exception {
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var timeslot = usr.getTimeslots().get(0);
		timeslotService.removeAttendeeFromTimeslot(timeslot, usr);
		TimeslotRequestBody timeslotRequestBody = new TimeslotRequestBody();
		timeslotRequestBody.setStudentId(usr.getId());
		timeslotRequestBody.setTimeslotId(timeslot.getId());
		mockMvc.perform(post("/api/v1/timeslots/addToWaitlist").contentType(MediaType.APPLICATION_JSON)
																			.content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
							.andExpect(status().isOk());
		assertTrue("User in waitlist", timeslotService.getById(timeslot.getId()).orElseThrow().getWaitList().contains(usr));
		mockMvc.perform(post("/api/v1/timeslots/addToWaitlist").contentType(MediaType.APPLICATION_JSON)
																			.content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
							.andExpect(status().isBadRequest());
	}
	
	
	@Test
	@WithMockUser(roles = "MEMBER")
	@Transactional
	void testAddToWaitlistBadRequest() throws Exception {
		userService.getByUsername("dscha001").orElseThrow();
		mockMvc.perform(post("/api/v1/timeslots/addToWaitlist"))
							.andExpect(status().isBadRequest());
	}
	
	@Test
	@WithMockUser(roles = "MEMBER")
	@Transactional
	void testRemoveUserFromWaitlist() throws Exception {
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var timeslot = usr.getTimeslots().get(0);
		timeslotService.removeAttendeeFromTimeslot(timeslot, usr);
		TimeslotRequestBody timeslotRequestBody = new TimeslotRequestBody();
		timeslotRequestBody.setStudentId(usr.getId());
		timeslotRequestBody.setTimeslotId(timeslot.getId());
		mockMvc.perform(post("/api/v1/timeslots/addToWaitlist").contentType(MediaType.APPLICATION_JSON)
															   .content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
			   .andExpect(status().isOk());
		assertTrue("User in waitlist", timeslotService.getById(timeslot.getId()).orElseThrow().getWaitList().contains(usr));
		
		mockMvc.perform(post("/api/v1/timeslots/removeFromWaitlist").contentType(MediaType.APPLICATION_JSON)
															   .content(new ObjectMapper().writeValueAsString(timeslotRequestBody)))
			   .andExpect(status().isOk());
		assertTrue("User no longer in waitlist", !timeslotService.getById(timeslot.getId()).orElseThrow().getWaitList().contains(usr));
	}
	
	@Test
	@WithMockUser(roles = "MEMBER")
	@Transactional
	void testRemoveFromWaitlistBadRequest() throws Exception {
		userService.getByUsername("dscha001").orElseThrow();
		mockMvc.perform(post("/api/v1/timeslots/removeFromWaitlist"))
			   .andExpect(status().isBadRequest());
	}
	
}
