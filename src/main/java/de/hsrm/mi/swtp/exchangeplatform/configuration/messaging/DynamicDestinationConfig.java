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
		connectionFactory.setExceptionListener(exception -> {
			log.info(String.format("Exception in \"topicConnection\": %s", exception.getErrorCode()));
			exception.printStackTrace();
		});
		log.info("Created timeslotConnection: ", topicConnection.toString());
		topicConnection.start();
		return topicConnection;
	}
	
	@Bean(name = "moduleConnection")
	public TopicConnection moduleConnection() throws JMSException {
		log.info(connectionFactory.getBrokerURL());
		TopicConnection moduleConnection =  connectionFactory.createTopicConnection();
		connectionFactory.setExceptionListener(exception -> {
			log.info(String.format("Exception in \"moduleConnection\": %s", exception.getErrorCode()));
			exception.printStackTrace();
		});
		log.info("Created moduleConnection: ", moduleConnection.toString());
		moduleConnection.start();
		return moduleConnection;
	}
	
	@Bean(name = "timeTableConnection")
	public TopicConnection timeTableConnection() throws JMSException {
		log.info(connectionFactory.getBrokerURL());
		TopicConnection timeTableConnection =  connectionFactory.createTopicConnection();
		connectionFactory.setExceptionListener(exception -> {
			log.info(String.format("Exception in \"timeTableConnection\": %s", exception.getErrorCode()));
			exception.printStackTrace();
		});
		log.info("Created timeTableConnection: ", timeTableConnection.toString());
		timeTableConnection.start();
		return timeTableConnection;
	}
	
	@Bean(name = "tradeOfferConnection")
	public TopicConnection tradeOfferConnection() throws JMSException {
		log.info(connectionFactory.getBrokerURL());
		TopicConnection tradeOfferConnection =  connectionFactory.createTopicConnection();
		connectionFactory.setExceptionListener(exception -> {
			log.info(String.format("Exception in \"tradeOfferConnection\": %s", exception.getErrorCode()));
			exception.printStackTrace();
		});
		log.info("Created tradeOfferConnection: ", tradeOfferConnection.toString());
		tradeOfferConnection.start();
		return tradeOfferConnection;
	}
	
	@Bean(name = "poConnection")
	public TopicConnection poConnection() throws JMSException {
		log.info(connectionFactory.getBrokerURL());
		TopicConnection poConnection =  connectionFactory.createTopicConnection();
		connectionFactory.setExceptionListener(exception -> {
			log.info(String.format("Exception in \"poConnection\": %s", exception.getErrorCode()));
			exception.printStackTrace();
		});
		log.info("Created tradeOfferConnection: ", poConnection.toString());
		poConnection.start();
		return poConnection;
	}
	
}
