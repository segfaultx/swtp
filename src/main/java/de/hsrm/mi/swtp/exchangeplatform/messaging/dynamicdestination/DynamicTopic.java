package de.hsrm.mi.swtp.exchangeplatform.messaging.dynamicdestination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.TopicSession;

/**
 * Just a simple DTO for transferring a TopicSession and an ActiveMQTopic.
 * Is mainly used by {@link de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.AbstractDynamicTopicManager}.
 */
@Value
@Builder
@AllArgsConstructor
public class DynamicTopic {
	TopicSession topicSession;
	ActiveMQTopic topic;
}