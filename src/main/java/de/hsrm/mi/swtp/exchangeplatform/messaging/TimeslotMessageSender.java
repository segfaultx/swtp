package de.hsrm.mi.swtp.exchangeplatform.messaging;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TimeslotMessageSender {
    private JmsTemplate jmsTemplate;

    public void send() {
        jmsTemplate.send(TimeslotMessageListener.QUEUENAME, s -> s.createTextMessage("hello queue world"));
    }
}
