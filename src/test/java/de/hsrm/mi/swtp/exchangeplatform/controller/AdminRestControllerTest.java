package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.AdminSettingsRequest;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.CustomPythonFilterRequest;
import de.hsrm.mi.swtp.exchangeplatform.repository.AdminSettingsRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CustomPythonFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.utils.FilterUtils;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;


import javax.transaction.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class AdminRestControllerTest extends BaseRestTest {
	@Autowired
	AdminSettingsRepository adminSettingsRepository;
	@Autowired
	FilterUtils filterUtils;
	
	@Autowired
	AdminSettingsService adminSettingsService;
	
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
	@Test
	@WithMockUser(roles = "ADMIN")
	void testGetAllFilters() throws Exception {
		var result = new ObjectMapper().readValue(mockMvc.perform(get("/api/v1/admin/settings/filters"))
		.andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString(), List.class);
		assertEquals("All Filters equals", filterUtils.getAllAvailableFilters(), result);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void testCreateCustomPythonFilter() throws Exception {
		CustomPythonFilterRequest json = new CustomPythonFilterRequest();
		json.setCode("print 'hello'");
		json.setFilterName("testFilter");
		var result = new ObjectMapper().readValue(mockMvc.perform(post("/api/v1/admin/customfilters").contentType(MediaType.APPLICATION_JSON)
									.content(new ObjectMapper().writeValueAsString(json)))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString(), CustomPythonFilter.class);
		assertEquals("Custom python filter name equals", json.getFilterName(), result.getFilterName());
		assertEquals("Custom python filter code equals", json.getCode(), result.getPythonCode());
		assertTrue("Filter registered in system", filterUtils.filterExists("testFilter"));
	}
	@Test
	@WithMockUser(roles = "ADMIN")
	void testGetCustoMpythonFilterTemplate() throws Exception {
		Resource resource = new ClassPathResource("Beispielskript.py");
		StringBuilder builder = new StringBuilder();
		InputStream inputStream = resource.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line = reader.readLine();
		while(line != null){
			builder.append(line);
			builder.append('\n'); // preserve line breaks
			line = reader.readLine();
		}
		mockMvc.perform(get("/api/v1/admin/filtertemplate"))
			   .andExpect(status().isOk())
			   .andExpect(content().string(builder.toString()));
	}
	@Test
	@WithMockUser(roles = "ADMIN")
	void testTradingActive() throws Exception {
		mockMvc.perform(get("/api/v1/admin/tradingActive"))
			   .andExpect(status().isOk())
			   .andExpect(content().string(Boolean.toString(adminSettingsService.isTradesActive())));
	}
	
}