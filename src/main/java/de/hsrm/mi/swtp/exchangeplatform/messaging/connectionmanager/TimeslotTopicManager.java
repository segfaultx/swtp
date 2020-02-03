package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.configuration.messaging.DynamicDestinationConfig;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * Can create a connection for each {@link Timeslot} and manage it.
 *
 * @see AbstractDynamicTopicManager
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TimeslotTopicManager extends AbstractDynamicTopicManager<Timeslot> {
	
	/**
	 * @see DynamicDestinationConfig#timeslotConnection()
	 */
	TopicConnection timeslotConnection;
	
	@Override
	public Topic createTopic(Timeslot obj) {
		Topic topic;
		try {
			createTopic(obj.getId(), timeslotConnection);
			
			topic = this.getTopic(obj.getId());
			final String topicName = topic.getTopicName();
			final TopicSession topicSession = this.getSession(obj.getId());
			
			topicSession.createSubscriber(topic).setMessageListener(message -> {
				try {
					log.info(String.format("-> Timeslot %s received: \"%s\"", obj.getId(), ((TextMessage) message).getText()));
				} catch(JMSException e) {
					e.printStackTrace();
				}
			});
			topicSession.createPublisher(topic)
						.publish(topicSession.createTextMessage(String.format("Timeslot %s: Topic %s is now alive", obj.getId(), topicName)));
			
		} catch(NullPointerException e) {
			log.error(String.format("Could not create Topic for null."));
			return null;
		} catch(JMSException e) {
			log.error(String.format("Could not create Topic for Timeslot %s.", obj.getId()));
			return null;
		}
		return topic;
	}
	
}
