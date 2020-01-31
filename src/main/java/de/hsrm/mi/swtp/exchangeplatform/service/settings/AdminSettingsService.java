package de.hsrm.mi.swtp.exchangeplatform.service.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.ExchangeplatformStatusMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.AdminSettingsRepository;
import de.hsrm.mi.swtp.exchangeplatform.event.admin.po.PORestrictionProcessorEventPublisher;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CustomPythonFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.utils.FilterUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ExchangeplatformMessageListener.TOPICNAME;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminSettingsService {
	
	private final Long adminSettingsId = 1L;
	@Autowired
	AdminSettingsRepository adminSettingsRepository;
	@Setter
	AdminSettings adminSettings;
	@Autowired
	FilterUtils filterUtils;
	@Autowired
	JmsTemplate jmsTopicTemplate;
	@Autowired
	PORestrictionProcessorEventPublisher poRestrictionProcessorEventPublisher;

	/**
	 * Method to provide trades restendpoint information if trades are active
	 *
	 * @return trades active status
	 */
	public boolean isTradesActive() {
		return adminSettings.isTradesActive();
	}

	@PreAuthorize("hasRole('ADMIN')")
	public boolean updateAdminSettings(boolean tradesActive,
									   List<String> activeFilters,
									   LocalDateTime startDateTrades,
									   LocalDateTime endDateTrades) throws NotFoundException {
		adminSettings.setTradesActive(tradesActive);
		adminSettings.setDateStartTrades(startDateTrades);
		adminSettings.setDateEndTrades(endDateTrades);
		for(String filter: activeFilters) {
			if(!filterUtils.filterExists(filter)) {
				throw new NotFoundException(String.format("Filter with name %s was not found", filter));
			}
		}
		
		adminSettings.setActiveFilters(activeFilters);
		filterUtils.setActiveFilters(activeFilters);

		if(tradesActive) {
			poRestrictionProcessorEventPublisher.execute();
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
	
	@PreAuthorize("hasRole('ADMIN')")
	public List<String> getAllFilters(){
		return filterUtils.getAllAvailableFilters();
	}
	
	public CustomPythonFilter addCustomPythonFilter(String filterName, String code){
		return filterUtils.addPythonFilter(filterName, code);
	}
}
