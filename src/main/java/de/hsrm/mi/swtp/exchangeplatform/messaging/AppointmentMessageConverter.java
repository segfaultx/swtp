package de.hsrm.mi.swtp.exchangeplatform.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        Appointment appointment = (Appointment) object;
        String jsontext = null;
        try {
            jsontext = mapper.writeValueAsString(appointment);
        } catch (JsonProcessingException e) {
            log.error("FEHLER toMessage '{}' -> JSON: {}", appointment.toString(), e.getMessage());
        }
        TextMessage message = session.createTextMessage(jsontext);
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String jsontext = textMessage.getText();

        Appointment appointment = null;
        try {
            appointment = mapper.readValue(jsontext, Appointment.class);
        } catch (JsonProcessingException e) {
            log.error("FEHLER fromMessage JSON '{}' -> Appointment", jsontext, e.getMessage());
        }
        return appointment;
    }
}
