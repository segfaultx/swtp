package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.AdminSettingsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import javax.transaction.Transactional;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AdminSettingsTest {
	@Autowired
	MockMvc mockmvc;
	@Autowired
	AdminSettingsRepository adminSettingsRepository;
	
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@Transactional
	void testGetAdminsettings() throws Exception {
		var result = mockmvc.perform(get("/api/v1/admin/settings")).andExpect(status().isOk()).andReturn();
		var deserialized_result = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AdminSettings.class);
		var actual_setting = adminSettingsRepository.findById(1L).orElseThrow();
		assertEquals("Adminsettings get equals", actual_setting, deserialized_result);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@Transactional
	void testPostAdminsettings() throws Exception {
		var actual_setting = adminSettingsRepository.findById(1L).orElseThrow();
		AdminSettings newAdminsetting = new AdminSettings();
		BeanUtils.copyProperties(actual_setting, newAdminsetting);
		newAdminsetting.setTradesActive(false);
		var result = mockmvc.perform(
				post("/api/v1/admin/settings").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(newAdminsetting)))
							.andExpect(status().isOk())
							.andReturn();
		var changed_setting = adminSettingsRepository.findById(1L).orElseThrow();
		var settings_response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AdminSettings.class);
		assertEquals("Adminsettings post equals", changed_setting, settings_response);
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	@Transactional
	void testFalseAdminsettingsPost() throws Exception{
		mockmvc.perform(post("/api/v1/admin/settings")
								.contentType(MediaType.APPLICATION_JSON).content("null"))
			   .andExpect(status().isBadRequest());
	}
}
