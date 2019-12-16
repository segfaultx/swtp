package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.messaging.converter.ExchangeplatformMessageConverter;
import de.hsrm.mi.swtp.exchangeplatform.messaging.converter.ModuleMessageConverter;
import de.hsrm.mi.swtp.exchangeplatform.messaging.converter.StudentMessageConverter;
import de.hsrm.mi.swtp.exchangeplatform.messaging.converter.TimeslotMessageConverter;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ExchangeplatformMessageListener;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ModuleMessageListener;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.StudentMessageListener;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.TimeslotMessageListener;
import de.hsrm.mi.swtp.exchangeplatform.service.JmsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.JndiDestinationResolver;

import javax.jms.ConnectionFactory;

@Slf4j
@EnableJms
@Configuration
public class MessageListenerConfig {
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Bean
	public BrokerService broker() throws Exception {
		log.info("BrokerService broker() gezogen");
		BrokerService broker = new BrokerService();
		broker.addConnector("tcp://0.0.0.0:4242");
		return broker;
	}
	
	@Bean
	public JndiDestinationResolver jndiDestinationResolver() {
		return new JndiDestinationResolver();
	}
	
	@Bean(name = "studentQueue")
	public ActiveMQQueue studentQueue() {
		return new ActiveMQQueue(StudentMessageListener.QUEUENAME);
	}
	
	@Bean(name = "timeslotQueue")
	public ActiveMQQueue timeslotQueue() {
		return new ActiveMQQueue(TimeslotMessageListener.QUEUENAME);
	}
	
	@Bean(name = "timeslotTopic")
	public ActiveMQTopic timeslotTopic() {
		return new ActiveMQTopic(TimeslotMessageListener.TOPICNAME);
	}
	
	@Bean(name = "moduleQueue")
	public ActiveMQQueue moduleQueue() {
		return new ActiveMQQueue(ModuleMessageListener.QUEUENAME);
	}
	
	@Bean(name = "moduleTopic")
	public ActiveMQTopic moduleTopic() {
		return new ActiveMQTopic(ModuleMessageListener.TOPICNAME);
	}
	
	@Bean(name = "exchangeplatformTopic")
	public ActiveMQTopic exchangeplatformTopic() {
		return new ActiveMQTopic(ExchangeplatformMessageListener.TOPICNAME);
	}
	
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setDestinationResolver(jndiDestinationResolver());
		jmsTemplate.setMessageIdEnabled(true);
		jmsTemplate.setMessageTimestampEnabled(true);
		jmsTemplate.setConnectionFactory(connectionFactory);
		return jmsTemplate;
	}
	
	@Bean(name = "timeslotTopicFactory")
	public DefaultJmsListenerContainerFactory timeslotTopicFactory() {
		log.info("DefaultJmsListenerContainerFactory::timeslotTopicFactory bean created");
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(true);
		return factory;
	}
	
	@Bean(name = "studentTopicFactory")
	public DefaultJmsListenerContainerFactory studentTopicFactory() {
		log.info("DefaultJmsListenerContainerFactory::studentTopicFactory bean created");
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(true);
		return factory;
	}
	
	@Bean(name = "moduleTopicFactory")
	public DefaultJmsListenerContainerFactory moduleTopicFactory() {
		log.info("DefaultJmsListenerContainerFactory::moduleTopicFactory bean created");
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(true);
		return factory;
	}
	
	@Bean(name = "timeslotQueueFactory")
	public DefaultJmsListenerContainerFactory timeslotQueueFactory() {
		log.info("DefaultJmsListenerContainerFactory timeslotQueueFactory() created");
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(false);
		factory.setMessageConverter(new TimeslotMessageConverter());
		factory.setErrorHandler(new JmsErrorHandler());
		return factory;
	}
	
	@Bean(name = "studentQueueFactory")
	public DefaultJmsListenerContainerFactory studentQueueFactory() {
		log.info("DefaultJmsListenerContainerFactory studentQueueFactory() created");
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(false);
		factory.setMessageConverter(new StudentMessageConverter());
		factory.setErrorHandler(new JmsErrorHandler());
		return factory;
	}
	
	@Bean(name = "moduleQueueFactory")
	public DefaultJmsListenerContainerFactory moduleQueueFactory() {
		log.info("DefaultJmsListenerContainerFactory moduleQueueFactory() created");
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(false);
		factory.setMessageConverter(new ModuleMessageConverter());
		factory.setErrorHandler(new JmsErrorHandler());
		return factory;
	}
	
	@Bean(name = "exchangeplatformFactory")
	public DefaultJmsListenerContainerFactory exchangeplatformFactory() {
		log.info("DefaultJmsListenerContainerFactory exchangeplatformFactory() created");
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setPubSubDomain(true);
		factory.setMessageConverter(new ExchangeplatformMessageConverter());
		factory.setErrorHandler(new JmsErrorHandler());
		return factory;
	}
	
}
