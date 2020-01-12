package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.TimeslotMessageListener;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeslotService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.HashMap;

/**
 * Can create a connection for each logged {@link User} and close those when the user goes offline.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TimeslotTopicManager implements DynamicTopicManager<Timeslot> {
	
	TimeslotRepository timeslotRepository;
	TimeslotService timeslotService;
	ActiveMQConnectionFactory connectionFactory;
	TopicConnection timeslotConnection;
	/** <Long, Topic> := Long -> Timeslot.id, Topic -> Topic of Timeslot */
	HashMap<Long, Topic> timeslotTopicHashMap;
	JmsTemplate jmsTopicTemplate;
	TimeslotMessageListener timeslotMessageListener;
	
	@Autowired
	@Builder
	public TimeslotTopicManager(final ActiveMQConnectionFactory connectionFactory, final TopicConnection timeslotConnection,
								final TimeslotRepository timeslotRepository, final TimeslotService timeslotService, JmsTemplate jmsTopicTemplate,
								TimeslotMessageListener timeslotMessageListener
							   ) {
		this.connectionFactory = connectionFactory;
		this.timeslotConnection = timeslotConnection;
		this.timeslotService = timeslotService;
		this.jmsTopicTemplate = jmsTopicTemplate;
		this.timeslotMessageListener = timeslotMessageListener;
		this.timeslotTopicHashMap = new HashMap<>();
		this.timeslotRepository = timeslotRepository;
	}
	
	private String createTopicName(final Timeslot timeslot) {
		return String.format(TOPIC_NAME_BASE, "Timeslot", timeslot.getId());
	}
	
	@Override
	public Topic createTopic(Timeslot obj) {
		Topic topic = null;
		try {
			final String topicName = createTopicName(obj);
			log.info(String.format(" + created topic name: %s", topicName));
			TopicSession topicSession = timeslotConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = topicSession.createTopic(topicName);
			log.info(String.format(" + created topic: %s", topic.toString()));
			this.timeslotTopicHashMap.put(obj.getId(), topic);
			
			topicSession.createSubscriber(topic).setMessageListener(message -> {
				try {
					log.info(String.format("-> Timeslot %s received: \"%s\"", obj.getId(), ((TextMessage) message).getText()));
				} catch(JMSException e) {
					e.printStackTrace();
				}
			});
			topicSession.createPublisher(topic)
						.publish(topicSession.createTextMessage(String.format("Timeslot %s: Topic %s is now alive", obj.getId(), topicName)));
			
		} catch(JMSException e) {
			log.error(String.format("Could not create Topic for Timeslot %s.", obj.getId()));
			return null;
		}
		return topic;
	}
	
}
