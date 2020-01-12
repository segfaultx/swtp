package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalConnectionManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.TradeOfferSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * A simple class which will provide methods for sending messages to a specific personal queue of a {@link User}.
 */
@Slf4j
@Component
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonalMessageSender {
	
	PersonalConnectionManager personalConnectionManager;
	JmsTemplate jmsTemplate;
	
	public void send(User user, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		this.send(personalConnectionManager.getQueue(user), tradeOfferSuccessfulMessage);
	}
	
	public void send(Long userId, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		this.send(personalConnectionManager.getQueue(userId), tradeOfferSuccessfulMessage);
	}
	
	public void send(ActiveMQQueue userQueue, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		jmsTemplate.send(userQueue, session -> session.createTextMessage(tradeOfferSuccessfulMessage.toString()));
	}
	
}