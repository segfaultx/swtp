package de.hsrm.mi.swtp.exchangeplatform.messaging.listener.admin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
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
public class AdminStudentStatusChangeMessageListener implements MessageListener {
	
	public final static String TOPICNAME = "ExchangeplatformAdminsTopic";
	
	@Override
	@JmsListener(destination = TOPICNAME)
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			log.info(String.format("outgoing %s -> \"%s\"", TOPICNAME, textMessage.getText()));
		} catch(JMSException e) {
			log.warn(String.format("RECEIVED MESSGE /w ERR // ON TOPIC -> %s :: %s", TOPICNAME, message.toString()));
		}
	}
}
