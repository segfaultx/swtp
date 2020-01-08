package de.hsrm.mi.swtp.exchangeplatform.configuration.messaging;

import de.hsrm.mi.swtp.exchangeplatform.messaging.TopicFactory;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ExchangeplatformMessageListener;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.JMSException;
import javax.jms.Topic;

@Slf4j
@EnableJms
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessagingDestinationConfig {
	
	ActiveMQConnectionFactory connectionFactory;
	
	@Bean(name = "exchangeplatformSettingsTopic")
	public Topic exchangeplatformSettingsTopic() throws JMSException {
		return TopicFactory.builder().connectionFactory(connectionFactory).build().createTopic(ExchangeplatformMessageListener.TOPICNAME);
	}
	
}
