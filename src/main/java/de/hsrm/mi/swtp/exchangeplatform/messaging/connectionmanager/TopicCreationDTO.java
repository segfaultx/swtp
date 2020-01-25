package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.TopicSession;

/** Just a simple DTO for transferring a TopicSession and an ActiveMQTopic */
@Value
@Builder
@AllArgsConstructor
public class TopicCreationDTO {
	TopicSession topicSession;
	ActiveMQTopic topic;
}