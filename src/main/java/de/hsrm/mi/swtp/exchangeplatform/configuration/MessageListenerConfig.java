package de.hsrm.mi.swtp.exchangeplatform.configuration;

import de.hsrm.mi.swtp.exchangeplatform.messaging.TopicFactory;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ExchangeplatformMessageListener;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;
import java.net.URI;

@Slf4j
@EnableJms
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageListenerConfig {
	
	private String brokerUrl = "tcp://0.0.0.0:4242";
	private String borkerUri = String.format("broker:(%s)", brokerUrl);
	
	@Bean(name = "broker")
	public BrokerService brokerService() throws Exception {
		BrokerService broker = BrokerFactory.createBroker(new URI(borkerUri));
		broker.start();
		return broker;
	}
	
	@Bean(name = "connectionFactory")
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setUserName("admin");
		activeMQConnectionFactory.setPassword("admin");
		activeMQConnectionFactory.setTrustAllPackages(true);
		
		return activeMQConnectionFactory;
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
	
	@Bean
	public ActiveMQConnectionFactory receiverActiveMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		return activeMQConnectionFactory;
	}
	
	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(receiverActiveMQConnectionFactory());
		return factory;
	}
	
	@Bean(name = "exchangeplatformSettingsTopic")
	public Topic exchangeplatformSettingsTopic() throws JMSException {
		return TopicFactory.builder()
						   .connectionFactory((ActiveMQConnectionFactory) connectionFactory())
						   .build()
						   .createTopic(ExchangeplatformMessageListener.TOPICNAME);
	}
	
}
