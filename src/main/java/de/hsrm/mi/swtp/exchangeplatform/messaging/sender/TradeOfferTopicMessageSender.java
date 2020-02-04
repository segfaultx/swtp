package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TradeOfferTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.TradeOfferCRUDMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;

/**
 * A simple class which will provide methods for sending messages to a {@link TradeOfferTopicManager#getTopic(Long)}.
 */
@Slf4j
@Component("tradeOfferTopicMessageSender")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeOfferTopicMessageSender implements MessageSender {
	
	TradeOfferTopicManager tradeOfferTopicManager;
	JmsTemplate jmsTopicTemplate;
	Topic tradeOffersTopic;
	
	public void notifyAllNewTradeOffer(TradeOffer newTradeOffer) {
		tradeOfferTopicManager.createTopic(newTradeOffer);
		final TradeOfferCRUDMessage message = TradeOfferCRUDMessage.builder()
																   .messageType(TradeOfferCRUDMessage.MessageType.TRADE_OFFER_CREATED)
																   .tradeOffer(newTradeOffer)
																   .build();
		jmsTopicTemplate.send(tradeOffersTopic,session -> {
			try {
				return session.createTextMessage(message.toJSON());
			} catch(JsonProcessingException e) {
				e.printStackTrace();
			}
			return session.createTextMessage("{}");
		});
	}
	
	public void notifyAllRemovedTradeOffer(TradeOffer tradeOffer) {
		tradeOfferTopicManager.getTopic(tradeOffer);
		final TradeOfferCRUDMessage message = TradeOfferCRUDMessage.builder()
																   .messageType(TradeOfferCRUDMessage.MessageType.TRADE_OFFER_REMOVED)
																   .tradeOffer(tradeOffer)
																   .build();
		jmsTopicTemplate.send(tradeOffersTopic,session -> {
			try {
				return session.createTextMessage(message.toJSON());
			} catch(JsonProcessingException e) {
				e.printStackTrace();
			}
			return session.createTextMessage("{}");
		});
	}
}