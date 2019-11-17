package de.hsrm.mi.swtp;

import de.hsrm.mi.swtp.exchangeplatform.messaging.Converter;
import de.hsrm.mi.swtp.exchangeplatform.service.JmsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import java.net.URI;

@Slf4j
@EnableJms
@SpringBootApplication
public class SwtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwtpApplication.class, args);
    }

    @Bean
    public BrokerService broker() throws Exception {
        log.info("BrokerService broker() gezogen");
        return BrokerFactory.createBroker(new URI("broker:(tcp://localhost:42424)"));
    }

    @Bean(name = "myFactory")
    public DefaultJmsListenerContainerFactory makeTopicFactory(ConnectionFactory connectionFactory) {
        log.info("DefaultJmsListenerContainerFactory myTopicFactory() gezogen");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // pub-sub = Publish-Subscribe = Topic-Kommunikationsmodell
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean(name = "myQueueFactory")
    public DefaultJmsListenerContainerFactory makeQueueFactory(ConnectionFactory connectionFactory) {
        log.info("DefaultJmsListenerContainerFactory myQueueFactory() gezogen");
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // pub-sub=false = Queue-Kommunikationsmodell
        factory.setPubSubDomain(false);
        factory.setMessageConverter(new Converter());
        factory.setErrorHandler(new JmsErrorHandler());
        return factory;
    }

}
