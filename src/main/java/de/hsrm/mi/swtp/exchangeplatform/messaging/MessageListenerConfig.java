package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.service.JmsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

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
    
    @Bean(name = "myTopicFactory")
    public DefaultJmsListenerContainerFactory makeTopicFactory() {
        log.info("DefaultJmsListenerContainerFactory myTopicFactory() gezogen");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }
    
    @Bean(name = "myQueueFactory")
    public DefaultJmsListenerContainerFactory makeQueueFactory() {
        log.info("DefaultJmsListenerContainerFactory myQueueFactory() gezogen");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        factory.setMessageConverter(new AppointmentMessageConverter());
        factory.setErrorHandler(new JmsErrorHandler());
        return factory;
    }
}
