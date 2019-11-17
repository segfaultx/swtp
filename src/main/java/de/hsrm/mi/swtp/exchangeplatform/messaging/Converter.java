package de.hsrm.mi.swtp.exchangeplatform.messaging;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class Converter implements MessageConverter {

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        return session.createTextMessage(object.toString());
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        return message.toString();
    }
}
