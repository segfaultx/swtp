package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TradeRequest;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeslotService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TradeOffersRestControllerTest extends BaseRestTest {
	@Autowired
	TradeOfferRepository tradeOfferRepository;
	
	@Autowired
	ModuleRepository moduleRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TimeslotRepository timeslotRepository;
	
	@Autowired
	TradeOfferService tradeOfferService;
	
	@Autowired
	TimeslotService timeslotService;
	@Autowired
	UserRepository userRepository;
	private String username = "dscha001";
	private String pass = "dscha001";
	
	@Test
	@Transactional
	void testCreateTradeOffer() throws Exception {
		var dennis = userRepository.findByUsername(username).orElseThrow();
		var tsDennis = dennis.getTimeslots().stream().filter(timeslot -> timeslot.getTimeSlotType() != TypeOfTimeslots.PRAKTIKUM).findFirst().orElseThrow();
		var tsDennisModule = tsDennis.getModule();
		var tsOther = timeslotRepository.findAllByModule(tsDennisModule)
										.stream()
										.filter(ts -> !ts.getId()
														 .equals(tsDennis.getId()) && (ts.getTimeSlotType() == TypeOfTimeslots.PRAKTIKUM || ts.getTimeSlotType() == TypeOfTimeslots.UEBUNG))
										.findFirst()
										.orElseThrow();
		var token = getLoginToken(username, pass);
		TradeRequest json = new TradeRequest();
		json.setOfferedByStudentMatriculationNumber(dennis.getId());
		json.setOfferedTimeslotId(tsDennis.getId());
		json.setWantedTimeslotId(tsOther.getId());
		var result = mockMvc.perform(post("/api/v1/trades/create").contentType(MediaType.APPLICATION_JSON)
																  .content(new ObjectMapper().writeValueAsString(json))
																  .header("Authorization", "Bearer " + token)).andExpect(status().isOk()).andReturn();
		var jsonString = result.getResponse().getContentAsString();
		var tradeofferId = JsonPath.read(jsonString, "$.id");
		var persistedTradeoffer = tradeOfferRepository.findById(Integer.toUnsignedLong((Integer) tradeofferId)).orElseThrow();
		assertNotNull("Tradeoffer null", persistedTradeoffer);
		assertEquals("Tradeoffer offered id", tsDennis.getId(), persistedTradeoffer.getOffer().getId());
		assertEquals("Tradeoffer seek id", tsOther.getId(), persistedTradeoffer.getSeek().getId());
		assertEquals("Offerer ID", dennis.getId(), persistedTradeoffer.getOfferer().getId());
		tradeOfferRepository.delete(persistedTradeoffer);
		tradeOfferRepository.flush();
	}
	
	@Test
	@Transactional
	void createAndDeleteTradeOffer() throws Exception {
		
		var token = getLoginToken(username, pass);
		TradeRequest json = new TradeRequest();
		var dennis = userRepository.findByUsername(username).orElseThrow();
		tradeOfferRepository.deleteAll(tradeOfferRepository.findAllByOfferer(dennis));
		
		// lookup random timeslot of type praktikum to offer for trade
		var tsDennis = dennis.getTimeslots().stream().filter(timeslot -> timeslot.getTimeSlotType() != TypeOfTimeslots.PRAKTIKUM).findFirst().orElseThrow();
		
		var tsDennisModule = tsDennis.getModule();
		// lookup other random timeslot to request a trade for, timeslot must be different from owned one
		var tsOther = timeslotRepository.findAllByModule(tsDennisModule)
										.stream()
										.filter(ts -> !ts.getId()
														 .equals(tsDennis.getId()) && (ts.getTimeSlotType() == TypeOfTimeslots.PRAKTIKUM || ts.getTimeSlotType() == TypeOfTimeslots.UEBUNG))
										.findFirst()
										.orElseThrow();
		json.setOfferedByStudentMatriculationNumber(dennis.getId());
		json.setOfferedTimeslotId(tsDennis.getId());
		json.setWantedTimeslotId(tsOther.getId());
		
		// create tradeoffer
		var result = mockMvc.perform(post("/api/v1/trades/create").contentType(MediaType.APPLICATION_JSON)
																  .content(new ObjectMapper().writeValueAsString(json))
																  .header("Authorization", "Bearer " + token)).andExpect(status().isOk()).andReturn();
		var jsonString = result.getResponse().getContentAsString();
		
		// delete tradeoffer
		mockMvc.perform(delete("/api/v1/trades/" + dennis.getId() + "/" + tsOther.getId()).header("Authorization", "Bearer " + token))
			   .andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	void testGetTradeOffersForTimeslot() throws Exception {
		var token = getLoginToken(username, pass);
		var dennis = userRepository.findByUsername("dscha001").orElseThrow();
		var tsDennis = dennis.getTimeslots().stream().filter(timeslot -> timeslot.getTimeSlotType() != TypeOfTimeslots.PRAKTIKUM).findFirst().orElseThrow();
		
		
		var result = mockMvc.perform(get("/api/v1/trades/" + tsDennis.getId()).header("Authorization", "Bearer " + token))
							.andExpect(status().isOk())
							.andReturn()
							.getResponse()
							.getContentAsString();
		var deserializedResult = new ObjectMapper().readValue(result, Map.class);
		assertNotNull("Tradeoffers Map null", deserializedResult);
		
	}
	@Test
	@WithMockUser(roles = "MEMBER", username = "dscha001", password = "dscha001")
	@Transactional
	void testGetAllMyTradeoffers() throws Exception {
		ObjectMapper reader = new ObjectMapper();
		reader.registerModule(new JavaTimeModule());
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var result = reader.readValue(
				mockMvc.perform(get("/api/v1/trades/mytradeoffers"))
					   .andExpect(status().isOk())
					   .andReturn()
					   .getResponse()
					   .getContentAsString(),
				List.class);
		var studTradeoffers = tradeOfferService.getAllTradeoffersForStudent(usr).stream().map(TradeOffer::getId)
				.collect(toList());
		for(var item: result){
			//TODO: check if serialization can be optimized, dirty workaround
			var longId = Long.valueOf(((LinkedHashMap)item).get("id").toString());
			assertTrue("Tradeoffer id contained in all tradeffers", studTradeoffers.contains(longId));
		}
	}
	@Test
	@WithMockUser(roles = "ADMIN", username = "wweit001", password = "wweit001")
	@Transactional
	void testAdminTrade() throws Exception {
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var timeslot = usr.getTimeslots().get(0);
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setWantedTimeslotId(timeslot.getId());
		tradeRequest.setOfferedTimeslotId(timeslot.getId());
		tradeRequest.setOfferedByStudentMatriculationNumber(usr.getId());
		mockMvc.perform(post("/api/v1/trades/admin")
					   .contentType(MediaType.APPLICATION_JSON)
					   .content(new ObjectMapper().writeValueAsString(tradeRequest)))
			   .andExpect(status().isOk());
	}
	
	
	//TODO: wait for filter fix to check wether test passes or not
	//@Test
	@Transactional
	void testTrade() throws Exception {
		var usr = userService.getByUsername("dscha001").orElseThrow();
		var studTradeofferTimeslot =  tradeOfferService.getAllTradeoffersForStudent(usr).get(0).getOffer();
		var acceptingTimeslot = timeslotRepository.findAllByModule(studTradeofferTimeslot.getModule())
												  .stream()
				.filter(item -> item.getTimeSlotType() == studTradeofferTimeslot.getTimeSlotType() && !item.getId().equals(studTradeofferTimeslot.getId()))
				.findFirst().orElseThrow();
		var acceptingUsr = acceptingTimeslot.getAttendees().get(0);
		TradeOffer acceptingTradeOffer = new TradeOffer();
		acceptingTradeOffer.setOfferer(acceptingUsr);
		acceptingTradeOffer.setOffer(acceptingTimeslot);
		acceptingTradeOffer.setSeek(studTradeofferTimeslot);
		tradeOfferRepository.saveAndFlush(acceptingTradeOffer);
		
		TradeRequest tradeRequest = new TradeRequest();
		tradeRequest.setOfferedByStudentMatriculationNumber(usr.getId());
		tradeRequest.setWantedTimeslotId(acceptingTimeslot.getId());
		tradeRequest.setOfferedTimeslotId(studTradeofferTimeslot.getId());
		
		var token = getLoginToken("dscha001", "dscha001");
		mockMvc.perform(post("/api/v1/trades")
					   .header("Authorization", "Bearer " + token)
					   .contentType(MediaType.APPLICATION_JSON)
					   .content(new ObjectMapper().writeValueAsString(tradeRequest)))
			   .andExpect(status().isOk());
		
	}
}
