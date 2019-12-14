package de.hsrm.mi.swtp.exchangeplatform.messaging.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.model.data.UserModel;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserMessageConverter implements MessageConverter {
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public Message toMessage(Object object, Session session) throws JMSException {
		UserModel user = (UserModel) object;
		String jsontext = null;
		try {
			log.info("TO MESSAGE::" + user.toString());
			jsontext = mapper.writeValueAsString(user);
			log.info("TEXT::" + jsontext);
		} catch(JsonProcessingException e) {
			log.error("FEHLER toMessage '{}' -> JSON: {}", user.toString(), e.getMessage());
		}
		return session.createTextMessage(jsontext);
	}
	
	@Override
	public Object fromMessage(Message message) throws JMSException {
		TextMessage textMessage = (TextMessage) message;
		String jsontext = textMessage.getText();
		
		UserModel user = null;
		try {
			user = mapper.readValue(jsontext, UserModel.class);
		} catch(JsonProcessingException e) {
			log.error("FEHLER fromMessage JSON '{}' -> Student", jsontext, e.getMessage());
		}
		return user;
	}
	
}
