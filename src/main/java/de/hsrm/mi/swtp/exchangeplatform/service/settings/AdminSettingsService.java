package de.hsrm.mi.swtp.exchangeplatform.service.settings;

import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.ExchangeplatformMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.AdminSettingsRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminSettingsService {
	AdminSettingsRepository adminSettingsRepository;
	AdminSettings adminSettings;
	final Long adminSettingsId = 1L;
	ExchangeplatformMessageSender exchangeplatformMessageSender;
	
	@Autowired
	public AdminSettingsService(@NotNull AdminSettingsRepository adminSettingsRepository) {
		this.adminSettingsRepository = adminSettingsRepository;
		var tmp = adminSettingsRepository.findById(adminSettingsId);
		if(tmp.isPresent())
			this.adminSettings = tmp.get(); // TODO: throw exception if settings not present at application startup, changed to this so DBInitiator can fill DB
		else log.info(String.format("Couldnt lookup admin settings with ID: %d", adminSettingsId));
	}
	
	/**
	 * Method to provide trades restendpoint information if trades are active
	 *
	 * @return trades active status
	 */
	public boolean isTradesActive() {
		return adminSettings.isTradesActive();
	}
	
	public void setAdminSettings(AdminSettings adminSettings) {
		this.adminSettings = adminSettings;
	}
	
	public boolean updateAdminSettings(boolean tradesActive, List<String> activeFilters){
		this.adminSettings.updateAdminSettings(tradesActive, activeFilters);
		exchangeplatformMessageSender.send(tradesActive);
		adminSettingsRepository.save(adminSettings);
		return true;
	}
	
	public AdminSettings getAdminSettings() {
		return adminSettings;
	}
}
