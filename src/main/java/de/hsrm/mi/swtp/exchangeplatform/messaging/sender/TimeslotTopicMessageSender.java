package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TimeslotTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.TimeslotUpdateMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicSession;

/**
 * A simple class which will provide methods for sending messages to a {@link TimeslotTopicManager#getTopic(Long)}.
 */
@Slf4j
@Component("timeslotTopicMessageSender")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeslotTopicMessageSender implements MessageSender {
	
	TimeslotTopicManager timeslotTopicManager;
	ObjectMapper objectMapper;
	JmsTemplate jmsTopicTemplate;
	
	public void notifyAll(@NonNull final Timeslot timeslot) {
		Topic topic = timeslotTopicManager.getTopic(timeslot);
		TopicSession session = timeslotTopicManager.getSession(timeslot);
		
		try {
			session.createSubscriber(topic).setMessageListener(message -> log.warn(String.format("--> outgoing to %s :: %s",
																								 topic.toString(),
																								 timeslot.toString())));
			jmsTopicTemplate.send(topic, session1 -> {
				try {
					return session.createTextMessage(TimeslotUpdateMessage.builder()
																		  .timeslot(timeslot)
																		  .build()
																		  .toJSON());
				} catch(JsonProcessingException e) {
					return session.createTextMessage("{}");
				}
			});
		} catch(JMSException e) {
			log.warn(String.format("TIMESLOT UPDATE MESSAGE NOT SENT DUE TO %s.", e.getCause().getClass()));
		}
	}
	
}