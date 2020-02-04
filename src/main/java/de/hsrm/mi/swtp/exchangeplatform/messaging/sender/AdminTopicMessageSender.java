package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.POTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.AdminStudentStatusChangeMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;

/**
 * A simple class which will provide methods for sending messages to a specific personal queue of a {@link User}.
 */
@Slf4j
@Component("adminTopicMessageSender")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminTopicMessageSender implements MessageSender {
	
	POTopicManager poTopicManager;
	JmsTemplate jmsTopicTemplate;
	ObjectMapper objectMapper;
	Topic adminNotificationsTopics;
	
	public void send(AdminStudentStatusChangeMessage message) {
		jmsTopicTemplate.send(adminNotificationsTopics, session -> {
			try {
				return session.createTextMessage(objectMapper.writeValueAsString(message));
			} catch(JsonProcessingException e) {
				e.printStackTrace();
			}
			return session.createTextMessage("{}");
		});
	}
	
}