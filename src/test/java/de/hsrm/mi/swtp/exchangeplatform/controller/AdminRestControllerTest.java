package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.AdminSettingsRequest;
import de.hsrm.mi.swtp.exchangeplatform.repository.AdminSettingsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;


import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class AdminRestControllerTest extends BaseRestTest {
	@Autowired
	AdminSettingsRepository adminSettingsRepository;
	
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@Transactional
	void testGetAdminsettings() throws Exception {
		ObjectMapper serializer = new ObjectMapper();
		serializer.registerModule(new JavaTimeModule());
		var result = mockMvc.perform(get("/api/v1/admin/settings")).andExpect(status().isOk()).andReturn();
		var deserialized_result = serializer.readValue(result.getResponse().getContentAsString(), AdminSettings.class);
		var actual_setting = adminSettingsRepository.findById(1L).orElseThrow();
		assertEquals("Adminsettings get equals", actual_setting, deserialized_result);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@Transactional
	void testPostAdminsettings() throws Exception {
		ObjectMapper serializer = new ObjectMapper();
		serializer.registerModule(new JavaTimeModule());
		AdminSettingsRequest request = new AdminSettingsRequest();
		request.setActiveFilters(new ArrayList<>());
		request.setDateEndTrades(LocalDateTime.now());
		request.setDateStartTrades(LocalDateTime.now());
		request.setTradesActive(false);
		var result = mockMvc.perform(
				post("/api/v1/admin/settings").contentType(MediaType.APPLICATION_JSON).content(serializer.writeValueAsString(request)))
							.andExpect(status().isOk())
							.andReturn();
		var changed_setting = adminSettingsRepository.findById(1L).orElseThrow();
		var settings_response = serializer.readValue(result.getResponse().getContentAsString(), AdminSettings.class);
		assertEquals("Adminsettings post equals", changed_setting, settings_response);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@Transactional
	void testFalseAdminsettingsPost() throws Exception {
		mockMvc.perform(post("/api/v1/admin/settings")
								.contentType(MediaType.APPLICATION_JSON).content("null"))
			   .andExpect(status().isBadRequest());
	}
	@Test
	@WithMockUser(roles = "USER")
	void testUnauthorized() throws Exception {
		mockMvc.perform(get("/api/v1/admin/settings")).andExpect(status().is4xxClientError());
	}
}