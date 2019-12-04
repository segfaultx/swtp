package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@Builder
public class TimeslotMessageSender implements MessageSender<Timeslot> {
	
	JmsTemplate jmsTemplate;
	ActiveMQQueue timeslotQueue;
	
	@Override
	public void send() {
		log.info("TIMESLOT WURDE ABGERUFEN");
		this.send("TIMESLOT WURDE ABGERUFEN");
	}
	
	@Override
	public void send(Timeslot model) {
		String message = String.format("Create Timeslot-Event TextMessage::< TIMESLOT {} WURDE ABGERUFEN/-GEÄNDERT. >", model.getId());
		this.send(message);
	}
	
	@Override
	public void send(String message) {
		log.info(message);
		jmsTemplate.send(timeslotQueue, session -> session.createTextMessage(message));
	}
	
}
