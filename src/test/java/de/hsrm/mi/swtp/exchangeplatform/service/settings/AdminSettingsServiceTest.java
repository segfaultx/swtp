package de.hsrm.mi.swtp.exchangeplatform.service.settings;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AdminSettingsServiceTest {

	AdminSettingsService settingsService;
	
	@Test
	@WithMockUser(username = "wweit001", roles = {"ADMIN", "MEMBER"})
	public void testUpdateAdminSettings() throws NotFoundException {
		settingsService.updateAdminSettings(false, new ArrayList<>());
	}
}
