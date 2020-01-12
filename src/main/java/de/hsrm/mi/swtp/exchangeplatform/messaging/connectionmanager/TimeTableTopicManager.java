package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.configuration.messaging.DynamicDestinationConfig;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.HashMap;

/**
 * Can create a connection for each {@link TimeTable}.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TimeTableTopicManager extends AbstractDynamicTopicManager<TimeTable> {
	
	/**
	 * @see DynamicDestinationConfig#timeTableConnection()
	 */
	TopicConnection timeTableConnection;
	/**
	 * A collection which saves all dynamically created Topics of {@link TimeTable TimeTables} and maps their
	 * {@link TimeTable#getId()} to the corresponding{@link Topic}.
	 * <p>
	 * <Long, Topic> := Long -> {@link TimeTable#getId()}, Topic -> Topic of Timeslot
	 */
	HashMap<Long, Topic> timeTableTopicHashMap;
	HashMap<Long, TopicSession> timeTableSessionHashMap;
	
	@Autowired
	@Builder
	public TimeTableTopicManager(final TopicConnection timeTableConnection) {
		this.timeTableConnection = timeTableConnection;
		this.timeTableTopicHashMap = new HashMap<>();
		this.timeTableSessionHashMap = new HashMap<>();
	}
	
	@Override
	public Topic createTopic(TimeTable obj) {
		Topic topic = null;
		try {
			final String topicName = createTopicName(obj.getId());
			log.info(String.format(" + created topic name: %s", topicName));
			TopicSession topicSession = timeTableConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = topicSession.createTopic(topicName);
			log.info(String.format(" + created topic: %s", topic.toString()));
			this.timeTableTopicHashMap.put(obj.getId(), topic);
			this.timeTableSessionHashMap.put(obj.getId(), topicSession);
			
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
	
	@Override
	public Topic getTopic(Long id) {
		return this.timeTableTopicHashMap.get(id);
	}
	
	@Override
	public Topic getTopic(TimeTable obj) {
		return this.getTopic(obj.getId());
	}
	
	@Override
	public TopicSession getSession(Long id) {
		return this.timeTableSessionHashMap.get(id);
	}
	
	@Override
	public TopicSession getSession(TimeTable obj) {
		return this.getSession(obj.getId());
	}
	
}
