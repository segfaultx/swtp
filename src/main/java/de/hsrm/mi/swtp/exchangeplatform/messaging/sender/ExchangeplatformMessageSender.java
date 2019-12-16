package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import de.hsrm.mi.swtp.exchangeplatform.messaging.ExchangeplatformStatusMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@Builder
public class ExchangeplatformMessageSender implements MessageSender<ExchangeplatformStatusMessage> {
	
	JmsTemplate jmsTemplate;
	ActiveMQTopic exchangeplatformTopic;
	
	@Override
	public void send() { }
	
	@Override
	public void send(ExchangeplatformStatusMessage statusMessage) {
		log.info(statusMessage.toString());
		jmsTemplate.send(exchangeplatformTopic, session -> session.createTextMessage(statusMessage.toString()));
	}
	
	@Override
	public void send(String message) {
		log.info(message);
		jmsTemplate.send(exchangeplatformTopic, session -> session.createTextMessage(message));
	}
	
	public void send(boolean isActive) {
		this.send(ExchangeplatformStatusMessage.builder()
											   .isActive(isActive)
											   .message(isActive ? "Exchangeplatform is now active."
																: "Exchangeplatform is now inactive.")
											   .build());
	}
	
}
