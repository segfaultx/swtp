package de.hsrm.mi.swtp.exchangeplatform.configuration;

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
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.JndiDestinationResolver;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import java.net.URI;
import java.util.Properties;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;

@Slf4j
@EnableJms
@Configuration
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
	
	@Bean(name = "connectionFactory")
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setUserName(USERNAME);
		activeMQConnectionFactory.setPassword(PASSWORD);
		activeMQConnectionFactory.setTrustAllPackages(true);
		
		return activeMQConnectionFactory;
	}
	
	@Bean(name = "broker")
	public BrokerService brokerService() throws Exception {
		BrokerService broker = BrokerFactory.createBroker(new URI(brokerUri));
		broker.start();
		return broker;
	}
	
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setDestinationResolver(jndiDestinationResolver());
		jmsTemplate.setMessageIdEnabled(true);
		jmsTemplate.setMessageTimestampEnabled(true);
		jmsTemplate.setConnectionFactory(connectionFactory());
		return jmsTemplate;
	}
	
}
