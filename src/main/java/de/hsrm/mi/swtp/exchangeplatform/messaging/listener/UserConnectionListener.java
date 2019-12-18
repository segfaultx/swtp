package de.hsrm.mi.swtp.exchangeplatform.messaging.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
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
public class UserConnectionListener implements MessageListener {
	
	@Override
	public void onMessage(Message message) {
		try {
			log.info(((TextMessage) message).getText());
		} catch(JMSException e) {
			log.info("RECEOVED SOME ERROR MESSAGE");
		}
	}
}
