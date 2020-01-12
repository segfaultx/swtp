package de.hsrm.mi.swtp.exchangeplatform.configuration.messaging;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.JMSException;
import javax.jms.TopicConnection;

@Slf4j
@EnableJms
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Import({ MessagingConfig.class })
public class DynamicDestinationConfig {
	
	BrokerService broker;
	ActiveMQConnectionFactory connectionFactory;
	
	@Bean(name = "timeslotConnection")
	public TopicConnection timeslotConnection() throws JMSException {
		log.info(connectionFactory.getBrokerURL());
		TopicConnection topicConnection =  connectionFactory.createTopicConnection();
		log.info("Created timeslotConnection: ", topicConnection.toString());
		topicConnection.start();
		return topicConnection;
	}
	
}
