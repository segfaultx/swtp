package de.hsrm.mi.swtp.exchangeplatform.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.ExchangeplatformStatusMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@EnableJms
@RequiredArgsConstructor
public class ExchangeplatformMessageListener implements MessageListener {
	
	public final static String TOPICNAME = "ExchangeplatformTopic";
	ObjectMapper objectMapper;
	
	@SneakyThrows
	@Override
	@JmsListener(destination = TOPICNAME)
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		ExchangeplatformStatusMessage statusMessage = objectMapper.readValue(textMessage.getText(), ExchangeplatformStatusMessage.class);
		log.info(String.format("outgoing %s -> \"%s\"", TOPICNAME, statusMessage.toString()));
	}
}
