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
	HashMap<Long, TopicCreationDTO> timeslotTopicSessionMap;
	
	@Autowired
	@Builder
	public TimeslotTopicManager(final TopicConnection timeslotConnection) {
		this.timeslotConnection = timeslotConnection;
		this.timeslotTopicSessionMap = new HashMap<>();
	}
	
	@Override
	public Topic createTopic(Timeslot obj) {
		Topic topic;
		try {
			TopicCreationDTO topicCreationDTO = createTopic(obj.getId(), timeslotConnection);
			
			topic = topicCreationDTO.getTopic();
			final String topicName = topic.getTopicName();
			final TopicSession topicSession = topicCreationDTO.getTopicSession();
			
			this.timeslotTopicSessionMap.put(obj.getId(), topicCreationDTO);
			
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
		return this.timeslotTopicSessionMap.get(id).getTopic();
	}
	
	@Override
	public Topic getTopic(Timeslot obj) {
		return this.getTopic(obj.getId());
	}
	
	@Override
	public TopicSession getSession(Long id) {
		return this.timeslotTopicSessionMap.get(id).getTopicSession();
	}
	
	@Override
	public TopicSession getSession(Timeslot obj) {
		return this.getSession(obj.getId());
	}
	
}
