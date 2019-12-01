package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.service.JmsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.JndiDestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

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

    @Bean(name = "studentDestination")
    public Destination studentDestination() {
        return new ActiveMQQueue(StudentMessageListener.QUEUENAME);
    }

    @Bean(name = "timeslotDestination")
    public Destination timeslotDestination() {
        return new ActiveMQQueue(TimeslotMessageListener.QUEUENAME);
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
        log.info("DefaultJmsListenerContainerFactory::defaultTopicFactory bean created");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean(name = "studentTopicFactory")
    public DefaultJmsListenerContainerFactory studentTopicFactory() {
        log.info("DefaultJmsListenerContainerFactory::defaultTopicFactory bean created");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean(name = "timeslotQueueFactory")
    public DefaultJmsListenerContainerFactory timeslotQueueFactory() {
        log.info("DefaultJmsListenerContainerFactory myQueueFactory() gezogen");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        factory.setMessageConverter(new TimeslotMessageConverter());
        factory.setErrorHandler(new JmsErrorHandler());
        return factory;
    }

    @Bean(name = "studentQueueFactory")
    public DefaultJmsListenerContainerFactory studentQueueFactory() {
        log.info("DefaultJmsListenerContainerFactory myQueueFactory() gezogen");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        factory.setMessageConverter(new StudentMessageConverter());
        factory.setErrorHandler(new JmsErrorHandler());
        return factory;
    }

}
