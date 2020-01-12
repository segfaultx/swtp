package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.configuration.messaging.DynamicDestinationConfig;
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
 * Can create a connection for each {@link Timeslot}.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TimeslotTopicManager extends AbstractDynamicTopicManager<Timeslot> {
	
	/**
	 * @see DynamicDestinationConfig#timeslotConnection()
	 */
	TopicConnection timeslotConnection;
	/**
	 * A collection which saves all dynamically created Topics of {@link Timeslot Timeslots} and maps their
	 * {@link Timeslot#getId()} to the corresponding{@link Topic}.
	 * <p>
	 * <Long, Topic> := Long -> Timeslot.id, Topic -> Topic of Timeslot
	 */
	HashMap<Long, Topic> timeslotTopicHashMap;
	HashMap<Long, TopicSession> timeslotSessionHashMap;
	
	@Autowired
	@Builder
	public TimeslotTopicManager(final TopicConnection timeslotConnection
							   ) {
		this.timeslotConnection = timeslotConnection;
		this.timeslotTopicHashMap = new HashMap<>();
		this.timeslotSessionHashMap = new HashMap<>();
	}
	
	@Override
	public Topic createTopic(Timeslot obj) {
		Topic topic = null;
		try {
			final String topicName = createTopicName(obj.getId());
			log.info(String.format(" + created topic name: %s", topicName));
			TopicSession topicSession = timeslotConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = topicSession.createTopic(topicName);
			log.info(String.format(" + created topic: %s", topic.toString()));
			this.timeslotTopicHashMap.put(obj.getId(), topic);
			this.timeslotSessionHashMap.put(obj.getId(), topicSession);
			
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
	
	@Override
	public Topic getTopic(Long id) {
		return this.timeslotTopicHashMap.get(id);
	}
	
	@Override
	public Topic getTopic(Timeslot obj) {
		return this.getTopic(obj.getId());
	}
	
	@Override
	public TopicSession getSession(Long id) {
		return this.timeslotSessionHashMap.get(id);
	}
	
	@Override
	public TopicSession getSession(Timeslot obj) {
		return this.getSession(obj.getId());
	}
	
}
