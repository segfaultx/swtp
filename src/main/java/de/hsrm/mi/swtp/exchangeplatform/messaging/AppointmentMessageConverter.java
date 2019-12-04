package de.hsrm.mi.swtp.exchangeplatform.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class AppointmentMessageConverter implements MessageConverter {
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public Message toMessage(Object object, Session session) throws JMSException {
		Timeslot timeslot = (Timeslot) object;
		String jsontext = null;
		try {
			jsontext = mapper.writeValueAsString(timeslot);
		} catch(JsonProcessingException e) {
			log.error("FEHLER toMessage '{}' -> JSON: {}", timeslot.toString(), e.getMessage());
		}
		TextMessage message = session.createTextMessage(jsontext);
		return message;
	}
	
	@Override
	public Object fromMessage(Message message) throws JMSException {
		TextMessage textMessage = (TextMessage) message;
		String jsontext = textMessage.getText();
		
		Timeslot timeslot = null;
		try {
			timeslot = mapper.readValue(jsontext, Timeslot.class);
		} catch(JsonProcessingException e) {
			log.error("FEHLER fromMessage JSON '{}' -> Appointment", jsontext, e.getMessage());
		}
		return timeslot;
	}
}
