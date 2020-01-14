package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
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

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
		json.setOfferedTimeslotId(11);
		json.setWantedTimeslotId(23);
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
		assertEquals("Tradeoffer offered id",11L, persistedTradeoffer.getOffer().getId());
		assertEquals("Tradeoffer seek id",23L, persistedTradeoffer.getSeek().getId());
		assertEquals("Offerer ID",8L, persistedTradeoffer.getOfferer().getId());
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
