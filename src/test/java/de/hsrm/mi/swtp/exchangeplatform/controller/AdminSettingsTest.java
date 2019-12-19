package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WithMockUser(username = "wweit001", password = "wweit001", roles = {"ADMIN"})
public class AdminSettingsTest {
	@Autowired
	MockMvc mockmvc;
	@Autowired
	AdminSettingsService adminSettingsService;
	
	
	@Test
	void testGetAdminsettings() throws Exception{
		var result = mockmvc.perform(get("/api/v1/admin/settings"))
		.andExpect(status().isOk()).andReturn();
		var deserialized_result = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AdminSettings.class);
		User user = Mockito.mock(User.class);
		Authentication a = Mockito.mock(Authentication.class);
		SecurityContext sec = Mockito.mock(SecurityContext.class);
		AuthenticationProvider prov = Mockito.mock(AuthenticationProvider.class);
		var val = prov.authenticate(a);
		sec.setAuthentication(val);
		Mockito.when(sec.getAuthentication()).thenReturn(val);
		SecurityContextHolder.setContext(sec);
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
		assertEquals("Adminsettings get equals",adminSettingsService.getAdminSettings(), deserialized_result);
	}
}
