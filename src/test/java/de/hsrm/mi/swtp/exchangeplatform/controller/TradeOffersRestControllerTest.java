package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TradeRequest;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TradeOffersRestControllerTest extends BaseRestTest{
	@Autowired
	TradeOfferRepository tradeOfferRepository;
	
	private String username = "dscha001";
	private String pass = "dscha001";
	
	@Test
	void testCreateTradeOffer() throws Exception {
		var token = getLoginToken(username,pass);
		TradeRequest json = new TradeRequest();
		json.setOfferedByStudentMatriculationNumber(8);
		json.setOfferedTimeslotId(13);
		json.setWantedTimeslotId(24);
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
		assertEquals("Tradeoffer offered id",13L, persistedTradeoffer.getOffer().getId());
		assertEquals("Tradeoffer seek id",24L, persistedTradeoffer.getSeek().getId());
		assertEquals("Offerer ID",8L, persistedTradeoffer.getOfferer().getId());
		tradeOfferRepository.delete(persistedTradeoffer);
		tradeOfferRepository.flush();
	}
	
	@Test
	void createAndDeleteTradeOffer() throws Exception {
		var token = getLoginToken(username, pass);
		TradeRequest json = new TradeRequest();
		json.setOfferedByStudentMatriculationNumber(8);
		json.setOfferedTimeslotId(13);
		json.setWantedTimeslotId(24);
		var result = mockMvc.perform(post("/api/v1/trades/create")
											 .contentType(MediaType.APPLICATION_JSON)
											 .content(new ObjectMapper().writeValueAsString(json))
											 .header("Authorization", "Bearer " + token))
							.andExpect(status().isOk())
							.andReturn();
		var jsonString = result.getResponse().getContentAsString();
		mockMvc.perform(delete("/api/v1/trades/8/24")
								.header("Authorization", "Bearer " + token))
			   .andExpect(status().isOk());
	}
	
	@Test
	void testGetTradeOffersForModule() throws Exception {
		var token = getLoginToken(username, pass);
		var result = mockMvc.perform(get("/api/v1/trades/24")
									.header("Authorization", "Bearer " + token)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		var deserializedResult = new ObjectMapper().readValue(result, Map.class);
		assertNotNull("Tradeoffers Map null", deserializedResult);
		
	}
}
