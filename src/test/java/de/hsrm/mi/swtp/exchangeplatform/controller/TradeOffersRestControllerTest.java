package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TradeRequest;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TradeOffersRestControllerTest extends BaseRestTest{
	@Autowired
	TradeOfferRepository tradeOfferRepository;
	
	@Autowired
	ModuleRepository moduleRepository;
	
	@Autowired
	TimeslotRepository timeslotRepository;
	
	private String username = "dscha001";
	private String pass = "dscha001";
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	@Transactional
	void testCreateTradeOffer() throws Exception {
		var dennis = userRepository.findByUsername(username).orElseThrow();
		var tsDennis = dennis.getTimeslots()
					   .stream()
					   .filter(timeslot ->  timeslot.getTimeSlotType() != TypeOfTimeslots.PRAKTIKUM)
					   .findFirst()
					   .orElseThrow();
		var tsDennisModule = tsDennis.getModule();
		var tsOther = timeslotRepository.findAllByModule(tsDennisModule)
				.stream()
				.filter(ts -> !ts.getId().equals(tsDennis.getId())
						&& (ts.getTimeSlotType() == TypeOfTimeslots.PRAKTIKUM || ts.getTimeSlotType() == TypeOfTimeslots.UEBUNG))
				.findFirst()
				.orElseThrow();
		var token = getLoginToken(username,pass);
		TradeRequest json = new TradeRequest();
		json.setOfferedByStudentMatriculationNumber(dennis.getId());
		json.setOfferedTimeslotId(tsDennis.getId());
		json.setWantedTimeslotId(tsOther.getId());
		var result = mockMvc.perform(post("/api/v1/trades/create")
											 .contentType(MediaType.APPLICATION_JSON)
											 .content(new ObjectMapper().writeValueAsString(json))
											 .header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn();
		var jsonString = result.getResponse().getContentAsString();
		var tradeofferId = JsonPath.read(jsonString, "$.id");
		var persistedTradeoffer = tradeOfferRepository.findById(Integer.toUnsignedLong((Integer)tradeofferId)).orElseThrow();
		assertNotNull("Tradeoffer null", persistedTradeoffer);
		assertEquals("Tradeoffer offered id",tsDennis.getId(), persistedTradeoffer.getOffer().getId());
		assertEquals("Tradeoffer seek id",tsOther.getId(), persistedTradeoffer.getSeek().getId());
		assertEquals("Offerer ID",dennis.getId(), persistedTradeoffer.getOfferer().getId());
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
		var tsDennis = dennis.getTimeslots()
							 .stream()
							 .filter(timeslot ->  timeslot.getTimeSlotType() != TypeOfTimeslots.PRAKTIKUM)
							 .findFirst()
							 .orElseThrow();
		
		var tsDennisModule = tsDennis.getModule();
		// lookup other random timeslot to request a trade for, timeslot must be different from owned one
		var tsOther = timeslotRepository.findAllByModule(tsDennisModule)
										.stream()
										.filter(ts -> !ts.getId().equals(tsDennis.getId())
												&& (ts.getTimeSlotType() == TypeOfTimeslots.PRAKTIKUM || ts.getTimeSlotType() == TypeOfTimeslots.UEBUNG))
										.findFirst()
										.orElseThrow();
		json.setOfferedByStudentMatriculationNumber(dennis.getId());
		json.setOfferedTimeslotId(tsDennis.getId());
		json.setWantedTimeslotId(tsOther.getId());
		
		// create tradeoffer
		var result = mockMvc.perform(post("/api/v1/trades/create")
											 .contentType(MediaType.APPLICATION_JSON)
											 .content(new ObjectMapper().writeValueAsString(json))
											 .header("Authorization", "Bearer " + token))
							.andExpect(status().isOk())
							.andReturn();
		var jsonString = result.getResponse().getContentAsString();
		
		// delete tradeoffer
		mockMvc.perform(delete("/api/v1/trades/"+dennis.getId()+"/"+tsOther.getId())
								.header("Authorization", "Bearer " + token))
			   .andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	void testGetTradeOffersForTimeslot() throws Exception {
		var token = getLoginToken(username, pass);
		var dennis = userRepository.findByUsername("dscha001").orElseThrow();
		var tsDennis = dennis.getTimeslots()
							 .stream()
							 .filter(timeslot ->  timeslot.getTimeSlotType() != TypeOfTimeslots.PRAKTIKUM)
							 .findFirst()
							 .orElseThrow();
		
		
		var result = mockMvc.perform(get("/api/v1/trades/" + tsDennis.getId())
									.header("Authorization", "Bearer " + token)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		var deserializedResult = new ObjectMapper().readValue(result, Map.class);
		assertNotNull("Tradeoffers Map null", deserializedResult);
		
	}
}
