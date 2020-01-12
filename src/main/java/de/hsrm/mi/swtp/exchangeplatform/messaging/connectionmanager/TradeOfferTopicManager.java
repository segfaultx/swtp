package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.configuration.messaging.DynamicDestinationConfig;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.HashMap;

/**
 * Can create a connection for each {@link TradeOffer}.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TradeOfferTopicManager extends AbstractDynamicTopicManager<TradeOffer> {
	
	/**
	 * @see DynamicDestinationConfig#tradeOfferConnection()
	 */
	TopicConnection tradeOfferConnection;
	/**
	 * A collection which saves all dynamically created Topics of {@link TradeOffer TradeOffers} and maps their
	 * {@link Timeslot#getId()} to the corresponding{@link Topic}.
	 * <p>
	 * <Long, Topic> := Long -> {@link TradeOffer#getId()}, Topic -> Topic of Timeslot
	 */
	HashMap<Long, Topic> tradeOfferTopicHashMap;
	
	@Autowired
	@Builder
	public TradeOfferTopicManager(final TopicConnection tradeOfferConnection) {
		this.tradeOfferConnection = tradeOfferConnection;
		this.tradeOfferTopicHashMap = new HashMap<>();
	}
	
	@Override
	public Topic createTopic(TradeOffer obj) {
		Topic topic = null;
		try {
			final String topicName = createTopicName(obj.getId());
			log.info(String.format(" + created topic name: %s", topicName));
			TopicSession topicSession = tradeOfferConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = topicSession.createTopic(topicName);
			log.info(String.format(" + created topic: %s", topic.toString()));
			this.tradeOfferTopicHashMap.put(obj.getId(), topic);
			
			topicSession.createSubscriber(topic).setMessageListener(message -> {
				try {
					log.info(String.format("-> TradeOffer %s received: \"%s\"", obj.getId(), ((TextMessage) message).getText()));
				} catch(JMSException e) {
					e.printStackTrace();
				}
			});
			topicSession.createPublisher(topic)
						.publish(topicSession.createTextMessage(String.format("TradeOffer %s: Topic %s is now alive", obj.getId(), topicName)));
			
		} catch(JMSException e) {
			log.error(String.format("Could not create Topic for TradeOffer %s.", obj.getId()));
			return null;
		}
		return topic;
	}
	
}
