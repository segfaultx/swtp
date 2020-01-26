package de.hsrm.mi.swtp.exchangeplatform.messaging.factory;

import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TopicCreationDTO;
import lombok.Builder;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
@Builder
public class TopicFactory {
	
	ActiveMQConnectionFactory connectionFactory;
	
	/**
	 * @see #createTopicWithUsername(String, String, String)
	 */
	public Topic createTopic(final String topicName) throws JMSException {
		return this.createTopicWithUsername(topicName, null, null);
	}
	
	/**
	 * Creates a {@link Topic}, its own connection and session.
	 *
	 * @param topicName is the name of the {@link Topic} ({@link Topic#getTopicName()}).
	 * @param username  is the username of the topic.
	 * @param password  is the password of the user's username.
	 *
	 * @return a {@link Topic}.
	 */
	public Topic createTopicWithUsername(final String topicName, final String username, final String password) throws JMSException {
		TopicConnection connection;
		if(username == null) connection = connectionFactory.createTopicConnection();
		else connection = connectionFactory.createTopicConnection(username, password);
		TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(topicName);
		connection.start();
		return topic;
	}
	
	/**
	 * Creates a {@link Topic} and and session from a given connection.
	 *
	 * @param topicConnection is the connection through which the session is created.
	 * @param topicName is the name of the {@link Topic} ({@link Topic#getTopicName()}).
	 *
	 * @return a {@link Topic}.
	 */
	public TopicCreationDTO createTopic(final TopicConnection topicConnection, final String topicName) throws JMSException {
		TopicSession session = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(topicName);
		return TopicCreationDTO.builder().topicSession(session).topic((ActiveMQTopic) topic).build();
	}
	
}
