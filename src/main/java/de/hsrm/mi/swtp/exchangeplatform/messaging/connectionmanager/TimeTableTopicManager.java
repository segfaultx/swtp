package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.configuration.messaging.DynamicDestinationConfig;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * Can create a connection for each {@link TimeTable} and manage it.
 *
 * @see AbstractDynamicTopicManager
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TimeTableTopicManager extends AbstractDynamicTopicManager<TimeTable> {
	
	/**
	 * @see DynamicDestinationConfig#timeTableConnection()
	 */
	TopicConnection timeTableConnection;
	
	@Override
	public Topic createTopic(TimeTable obj) {
		Topic topic;
		try {
			createTopic(obj.getId(), timeTableConnection);
			
			topic = this.getTopic(obj.getId());
			final String topicName = topic.getTopicName();
			final TopicSession topicSession = this.getSession(obj.getId());
			
			topicSession.createSubscriber(topic).setMessageListener(message -> {
				try {
					log.info(String.format("-> TimeTable %s received: \"%s\"", obj.getId(), ((TextMessage) message).getText()));
				} catch(JMSException e) {
					e.printStackTrace();
				}
			});
			topicSession.createPublisher(topic)
						.publish(topicSession.createTextMessage(String.format("TimeTable %s: Topic %s is now alive", obj.getId(), topicName)));
			
		} catch(NullPointerException e) {
			log.error(String.format("Could not create Topic for null."));
			return null;
		} catch(JMSException e) {
			log.error(String.format("Could not create Topic for TimeTable %s.", obj.getId()));
			return null;
		}
		return topic;
	}
	
}
