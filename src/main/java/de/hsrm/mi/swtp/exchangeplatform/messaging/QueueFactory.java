package de.hsrm.mi.swtp.exchangeplatform.messaging;

import lombok.Builder;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
@Builder
public class QueueFactory {
	
	ActiveMQConnectionFactory connectionFactory;
	
	//TODO: check if needed, not used
	public Queue createQueue(final String queueName) throws JMSException {
		QueueConnection connection = connectionFactory.createQueueConnection();
		QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue(queueName);
		connection.start();
		return queue;
	}
	
}
