package de.hsrm.mi.swtp.exchangeplatform.messaging.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ModuleMessageConverter implements MessageConverter {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Message toMessage(Object object, Session session) throws JMSException {
        Module module = (Module) object;
        String jsontext = null;
        try {
            jsontext = mapper.writeValueAsString(module);
        } catch (JsonProcessingException e) {
            log.error("FEHLER toMessage '{}' -> JSON: {}", module.toString(), e.getMessage());
        }
        TextMessage message = session.createTextMessage(jsontext);
        return message;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String jsontext = textMessage.getText();

        Module module = null;
        try {
            module = mapper.readValue(jsontext, Module.class);
        } catch (JsonProcessingException e) {
            log.error("FEHLER fromMessage JSON '{}' -> Module", jsontext, e.getMessage());
        }
        return module;
    }
}
