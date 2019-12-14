package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import de.hsrm.mi.swtp.exchangeplatform.model.data.UserModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@Builder
public class UserMessageSender implements MessageSender<UserModel> {

	JmsTemplate jmsTemplate;
	ActiveMQQueue studentQueue;

	@Override
	public void send() {
		log.info("STUDENT WURDE ABGERUFEN");
		this.send("STUDENT WURDE ABGERUFEN");
	}

	@Override
	public void send(UserModel model) {
		String message = String.format("Create User-Event TextMessage::< USER {} WURDE ABGERUFEN/-GEÄNDERT. >", model.getId());
		this.send(message);
	}

	@Override
	public void send(String message) {
		log.info(message);
		jmsTemplate.send(studentQueue, session -> session.createTextMessage(message));
	}

}
