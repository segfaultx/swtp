package de.hsrm.mi.swtp.exchangeplatform.messaging.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@EnableJms
@RequiredArgsConstructor
public class TimeslotMessageListener implements MessageListener {
	
	@JmsListener(destination = "exchangeplatform:Timeslot-28")
	@Override
	public void onMessage(Message message) {
		try {
			log.info("======>");
			log.info(String.valueOf(((ObjectMessage) message).getObject()));
		} catch(JMSException e) {
			log.info("ERROR: " + message);
		}
	}
}
