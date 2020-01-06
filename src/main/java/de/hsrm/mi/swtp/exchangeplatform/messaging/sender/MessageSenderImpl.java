package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ExchangeplatformMessageListener;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.*;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static de.hsrm.mi.swtp.exchangeplatform.messaging.listener.ExchangeplatformMessageListener.TOPICNAME;

@Slf4j
@Builder
@Component
public class MessageSenderImpl implements MessageSender {
	
	ActiveMQConnectionFactory connectionFactory;
	JmsTemplate jmsTemplate;
	
	@Override
	public void send(final MessageProducer messageProducer, Message message) throws JMSException {
		log.info("sending message='{}'", message);
		messageProducer.send(message);
	}
	
	public void send(final String destination, String message) throws JMSException {
		ArrayList<MessageListener> consumers = new ArrayList<>();
		
//		for(int i = 0; i < 5; i++){
//			final int num = i+1;
//			consumers.add(new MessageListener() {
//				@Override
//				@JmsListener(destination = ExchangeplatformMessageListener.TOPICNAME)
//				public void onMessage(Message message1) {
//					log.info("asdfsgdhdafsgdfndfsefsgdng");
//					try {
//						log.info("MESSAGE to Listner-" + num + ": " + ((TextMessage) message1).getText());
//					} catch(JMSException e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
		
		log.info("sending message='{}'", message);
		jmsTemplate.send(TOPICNAME, session -> session.createTextMessage(message));
	}
}
