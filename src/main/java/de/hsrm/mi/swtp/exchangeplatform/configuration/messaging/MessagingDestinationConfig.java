package de.hsrm.mi.swtp.exchangeplatform.configuration.messaging;

import de.hsrm.mi.swtp.exchangeplatform.messaging.TopicFactory;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ExchangeplatformMessageListener;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeslotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;

@Slf4j
@EnableJms
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessagingDestinationConfig {
	
	BrokerService broker; // DO NOT REMOVE; NECESSARY
	ActiveMQConnectionFactory connectionFactory;
	TimeslotRepository timeslotRepository;
	TimeslotService timeslotService;
	TopicConnection timeslotConnection;
	
	/**
	 * A {@link Topic} used for publishing messages to basically any active client.
	 * @return
	 * @throws JMSException
	 */
	@Bean(name = "exchangeplatformSettingsTopic")
	public Topic exchangeplatformSettingsTopic() throws JMSException {
		return TopicFactory.builder()
						   .connectionFactory(connectionFactory)
						   .build()
						   .createTopic(ExchangeplatformMessageListener.TOPICNAME);
	}
	
}
