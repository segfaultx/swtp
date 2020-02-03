package de.hsrm.mi.swtp.exchangeplatform.configuration.messaging;

import de.hsrm.mi.swtp.exchangeplatform.messaging.factory.TopicFactory;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ExchangeplatformMessageListener;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.TradeOffersMessageListener;
import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.admin.AdminStudentStatusChangeMessageListener;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.JMSException;
import javax.jms.Topic;

@Slf4j
@EnableJms
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Import({ MessagingConfig.class })
public class MessagingDestinationConfig {
	
	TopicFactory topicFactory;
	BrokerService broker; // DO NOT REMOVE; NECESSARY!!
	
	/**
	 * A {@link Topic} used for publishing messages to basically any active client.
	 * @return
	 * @throws JMSException
	 */
	@Bean(name = "exchangeplatformSettingsTopic")
	public Topic exchangeplatformSettingsTopic() throws JMSException {
		return topicFactory.createTopic(ExchangeplatformMessageListener.TOPICNAME);
	}
	
	/**
	 * A {@link Topic} used for publishing messages to basically any active client.
	 * @return
	 * @throws JMSException
	 */
	@Bean(name = "adminNotificationsTopics")
	public Topic adminNotificationsTopics() throws JMSException {
		return topicFactory.createTopic(AdminStudentStatusChangeMessageListener.TOPICNAME);
	}
	
	/**
	 * A {@link Topic} used for publishing messages to basically any active client.
	 */
	@Bean(name = "tradeOffersTopic")
	public Topic tradeOffersTopic() throws JMSException {
		return topicFactory.createTopic(TradeOffersMessageListener.TOPICNAME);
	}
	
}
