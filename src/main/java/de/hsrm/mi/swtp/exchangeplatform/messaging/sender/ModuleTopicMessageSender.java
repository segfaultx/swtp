package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.ModuleTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.ModuleFullMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
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
 * A simple class which will provide methods for sending messages to a specific personal queue of a {@link User}.
 */
@Slf4j
@Component("moduleTopicMessageSender")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModuleTopicMessageSender {
	
	ModuleTopicManager moduleTopicManager;
	ObjectMapper objectMapper;
	JmsTemplate jmsTopicTemplate;
	
	public void notifyAllModuleFull(@NonNull final Module module) {
		Topic topic = moduleTopicManager.getTopic(module);
		TopicSession session = moduleTopicManager.getSession(module);
		
		try {
			session.createSubscriber(topic).setMessageListener(message -> log.warn(String.format("--> outgoing to %s :: %s",
																								 topic.toString(),
																								 module.toString())));
			jmsTopicTemplate.send(topic, session1 -> {
				try {
					return session.createTextMessage(ModuleFullMessage.builder()
																	  .module(module)
																	  .build()
																	  .toJSON());
				} catch(JsonProcessingException e) {
					return session.createTextMessage("{}");
				}
			});
		} catch(JMSException e) {
			log.warn(String.format("MODULE FULL MESSAGE NOT SENT DUE TO %s.", e.getCause().getClass()));
		}
	}
	
}