package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TradeRequest;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TradeOffersRestControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	TradeOfferRepository tradeOfferRepository;
	
	@Test
	void testCreateTradeOffer() throws Exception {
		var token = getLoginToken();
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
		var token = getLoginToken();
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
		var token = getLoginToken();
		var result = mockMvc.perform(get("/api/v1/trades/24")
									.header("Authorization", "Bearer " + token)).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		var deserializedResult = new ObjectMapper().readValue(result, Map.class);
		assertNotNull("Tradeoffers Map null", deserializedResult);
		
	}
	
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
