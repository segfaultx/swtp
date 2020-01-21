package de.hsrm.mi.swtp.exchangeplatform.service.settings;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.Assert.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AdminSettingsServiceTest {

	@Autowired
	private AdminSettingsService settingsService;
	
	@Test
	@WithMockUser(username = "wweit001", roles = {"ADMIN", "MEMBER"})
	public void testUpdateAdminSettingsDeactivateTrades() throws NotFoundException {
		settingsService.updateAdminSettings(false, new ArrayList<>());
		assertFalse(settingsService.isTradesActive());
	}
	
	@Test
	@WithMockUser(username = "wweit001", roles = {"ADMIN", "MEMBER"})
	public void testUpdateAdminSettingsActivateTrades() throws NotFoundException {
		settingsService.updateAdminSettings(true, new ArrayList<>());
		assertTrue(settingsService.isTradesActive());
	}
	
	@Test
	@WithMockUser(username = "wweit001", roles = {"ADMIN", "MEMBER"})
	public void testAdminSettingsNotNull() {
		assertNotNull(settingsService.getAdminSettings());
	}
	
}
