package de.hsrm.mi.swtp.exchangeplatform.service.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.ExchangeplatformStatusMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.AdminSettingsRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter.PORestrictionViolationProcessor;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.utils.FilterUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminSettingsService {
	
	final Long adminSettingsId = 1L;
	AdminSettingsRepository adminSettingsRepository;
	AdminSettings adminSettings;
	FilterUtils filterUtils;
	JmsTemplate jmsTopicTemplate;
	@Autowired
	PORestrictionViolationProcessor poRestrictionViolationProcessor;
	
	
	/**
	 *
	 * @param adminSettingsRepository
	 * @param jmsTopicTemplate
	 * @param filterUtils
	 */
	@Autowired
	public AdminSettingsService(@NotNull AdminSettingsRepository adminSettingsRepository,
								@NotNull JmsTemplate jmsTopicTemplate,
								@NotNull FilterUtils filterUtils) {
		this.adminSettingsRepository = adminSettingsRepository;
		this.jmsTopicTemplate = jmsTopicTemplate;
		this.filterUtils = filterUtils;
		var tmp = adminSettingsRepository.findAll();
		if(tmp.size() > 0)
			this.adminSettings = tmp.get(0); // TODO: throw exception if settings not present at application startup, changed to this so DBInitiator can fill DB
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
	public boolean updateAdminSettings(boolean tradesActive, List<String> activeFilters) throws ClassNotFoundException {
		adminSettings.setTradesActive(tradesActive);
		
		for(String filter: activeFilters) {
			if(!filterUtils.filterExists(filter)) {
				throw new ClassNotFoundException(String.format("Filter with name %s was not found", filter));
			}
		}
		
		adminSettings.setActiveFilters(activeFilters);
		filterUtils.setActiveFilters(activeFilters);

		if(tradesActive) {
			poRestrictionViolationProcessor.startProcessing();
//			poRestrictionViolationProcessorExecutor.execute();
		}

		jmsTopicTemplate.send(TOPICNAME, session -> {
			try {
				return session.createTextMessage(
						new ObjectMapper().writeValueAsString(new ExchangeplatformStatusMessage(tradesActive)));
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
