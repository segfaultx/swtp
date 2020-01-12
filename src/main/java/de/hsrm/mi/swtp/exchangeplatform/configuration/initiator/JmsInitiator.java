package de.hsrm.mi.swtp.exchangeplatform.configuration.initiator;

import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.ModuleTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TimeTableTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TimeslotTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TradeOfferTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeTableRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Initialises:
 * - all dynamically created {@link de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot} topics,
 * - all dynamically created {@link de.hsrm.mi.swtp.exchangeplatform.model.data.Module} topics,
 * - all dynamically created {@link de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable} topics,
 * - all dynamically created {@link de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer} topics,
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class JmsInitiator {
	
	TimeslotRepository timeslotRepository;
	TimeslotTopicManager timeslotTopicManager;
	
	ModuleRepository moduleRepository;
	ModuleTopicManager moduleTopicManager;
	
	TimeTableRepository timeTableRepository;
	TimeTableTopicManager timeTableTopicManager;
	
	TradeOfferRepository tradeOfferRepository;
	TradeOfferTopicManager tradeOfferTopicManager;
	
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		log.info("\n");
		log.info("//// START JmsInitiator");
		this.initTimeslotTopics();
		log.info("");
		this.initModuleTopics();
		log.info("");
		this.initTimeTableTopics();
		log.info("");
		this.initTradeOfferTopics();
		log.info("//// END JmsInitiator\n");
	}
	
	private void initTimeslotTopics() {
		List<Timeslot> timeslots = timeslotRepository.findAll();
		log.info("// START: Creating necessary Jms-Topics for Timeslots:");
		int number = 1;
		for(final Timeslot timeslot : timeslots) {
			log.info(String.format("%d) %s", number++, timeslot.toString()));
			timeslotTopicManager.createTopic(timeslot);
		}
		log.info("// END: Creating necessary Jms-Topics for Timeslots:");
	}
	
	private void initModuleTopics() {
		List<Module> modules = moduleRepository.findAll();
		log.info("// START: Creating necessary Jms-Topics for Modules:");
		int number = 1;
		for(final Module module : modules) {
			log.info(String.format("%d) %s", number++, module.toString()));
			moduleTopicManager.createTopic(module);
		}
		log.info("// END: Creating necessary Jms-Topics for Modules:");
	}
	
	private void initTimeTableTopics() {
		List<TimeTable> timeTables = timeTableRepository.findAll();
		log.info("// START: Creating necessary Jms-Topics for TimeTables:");
		int number = 1;
		for(final TimeTable timeTable : timeTables) {
			log.info(String.format("%d) %s", number++, timeTable.toString()));
			timeTableTopicManager.createTopic(timeTable);
		}
		log.info("// END: Creating necessary Jms-Topics for TimeTables:");
	}
	
	private void initTradeOfferTopics() {
		List<TradeOffer> tradeOffers = tradeOfferRepository.findAll();
		log.info("// START: Creating necessary Jms-Topics for TradeOffers:");
		int number = 1;
		for(final TradeOffer tradeOffer : tradeOffers) {
			log.info(String.format("%d) %s", number++, tradeOffer.toString()));
			tradeOfferTopicManager.createTopic(tradeOffer);
		}
		log.info("// END: Creating necessary Jms-Topics for TradeOffers:");
	}
	
}
