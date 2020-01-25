package de.hsrm.mi.swtp.exchangeplatform.configuration.messaging;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;
import javax.jms.Topic;
import java.net.URI;

@Slf4j
@EnableJms
@Configuration()
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessagingConfig {
	
	@Value("${spring.datasource.password}")
	String USERNAME;
	@Value("${spring.datasource.username}")
	String PASSWORD;
	@Value("${spring.activemq.broker-url}")
	String brokerUrl;
	@Value("${spring.activemq.broker-uri}")
	String brokerUri;
	BrokerService broker;
	
	@Bean(name = "connectionFactory")
	public ActiveMQConnectionFactory jmsConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setUserName(USERNAME);
		activeMQConnectionFactory.setPassword(PASSWORD);
		return activeMQConnectionFactory;
	}
	
	@Bean(name = "broker")
	public BrokerService brokerService() throws Exception {
		BrokerService broker = BrokerFactory.createBroker(new URI(brokerUri));
		broker.setBrokerName("exchangeplatform-broker");
		broker.deleteAllMessages();
		broker.setUseJmx(true);
		broker.setUseShutdownHook(false);
		broker.start(true);
		return broker;
	}
	
	/**
	 * A JmsTemplate used for {@link Queue Queues}.
	 * @return a basic JmsTemplate with which one can send messages to a {@link Queue}.
	 */
	@Bean(name = "jmsTemplate")
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setMessageIdEnabled(true);
		jmsTemplate.setMessageTimestampEnabled(true);
		jmsTemplate.setConnectionFactory(jmsConnectionFactory());
		return jmsTemplate;
	}
	
	/**
	 * A JmsTemplate used for {@link Topic Topics}.
	 * @return a basic JmsTemplate with which one can publish messages to a {@link Topic}.
	 */
	@Bean(name = "jmsTopicTemplate")
	public JmsTemplate jmsTopicTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setPubSubDomain(true);
		jmsTemplate.setMessageIdEnabled(true);
		jmsTemplate.setMessageTimestampEnabled(true);
		jmsTemplate.setConnectionFactory(jmsConnectionFactory());
		return jmsTemplate;
	}
	
}
