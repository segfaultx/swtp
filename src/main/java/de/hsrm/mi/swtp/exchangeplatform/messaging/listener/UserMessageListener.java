package de.hsrm.mi.swtp.exchangeplatform.messaging.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@EnableJms
@RequiredArgsConstructor
public class UserMessageListener implements MessageListener {
	
	public final static String QUEUENAME = "usr:171717171717-wweit001";
	
	@JmsListener(destination = QUEUENAME)
	@Override
	public void onMessage(Message message) {
		try {
			log.info(String.format("outgoing %s -> \"%s\"", QUEUENAME, ((TextMessage) message).getText()));
		} catch(JMSException e) {
			log.info("ERROR: " + message);
		}
	}
}
