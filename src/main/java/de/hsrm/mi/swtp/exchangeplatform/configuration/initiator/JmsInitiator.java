package de.hsrm.mi.swtp.exchangeplatform.configuration.initiator;

import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TimeslotTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class JmsInitiator {
	
	ActiveMQConnectionFactory connectionFactory;
	TimeslotRepository timeslotRepository;
	TimeslotTopicManager timeslotTopicManager;
	
	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		List<Timeslot> timeslots = timeslotRepository.findAll();
		log.info("// START: Creating necessary Jms-Topics for Timeslots:");
		int number = 1;
		for(final Timeslot timeslot : timeslots) {
			log.info(String.format("%d) %s", number++, timeslot.toString()));
			timeslotTopicManager.createTopic(timeslot);
		}
		log.info("// END: Creating necessary Jms-Topics for Timeslots:");
	}
	
}
