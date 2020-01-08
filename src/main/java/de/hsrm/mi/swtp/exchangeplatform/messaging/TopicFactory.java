package de.hsrm.mi.swtp.exchangeplatform.messaging;

import lombok.Builder;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
@Builder
public class TopicFactory {
	
	ActiveMQConnectionFactory connectionFactory;
	
	public Topic createTopic(final String topicName) throws JMSException {
		TopicConnection connection = connectionFactory.createTopicConnection();
		TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(topicName);
		connection.start();
		return topic;
	}
	
}
