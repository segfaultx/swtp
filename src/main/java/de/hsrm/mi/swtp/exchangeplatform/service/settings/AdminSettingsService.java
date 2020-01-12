package de.hsrm.mi.swtp.exchangeplatform.service.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.ExchangeplatformStatusMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.AdminSettingsRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

import static de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ExchangeplatformMessageListener.TOPICNAME;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminSettingsService {
	AdminSettingsRepository adminSettingsRepository;
	AdminSettings adminSettings;
	final Long adminSettingsId = 1L;
	@Autowired
	JmsTemplate jmsTopicTemplate;
	@Autowired
	ObjectMapper objectMapper;
	
	/**
	 *
	 * @param adminSettingsRepository
	 */
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
	
	
	/**
	 * Method to set the admin settings on startup, used for dev purposes
	 * @param adminSettings adminsettings from db
	 */
	public void setAdminSettings(AdminSettings adminSettings) {
		this.adminSettings = adminSettings;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public boolean updateAdminSettings(boolean tradesActive, List<String> activeFilters) throws NotFoundException {
		this.adminSettings.updateAdminSettings(tradesActive, activeFilters);
		jmsTopicTemplate.send(TOPICNAME, session -> {
			try {
				return session.createTextMessage(
						objectMapper.writeValueAsString(new ExchangeplatformStatusMessage(tradesActive)));
			} catch(JsonProcessingException e) {
				e.printStackTrace();
			}
			return session.createTextMessage("{}");
		});
		adminSettingsRepository.save(adminSettings);
		return true;
	}
	
	/**
	 *
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	public AdminSettings getAdminSettings() {
		return adminSettings;
	}
}
