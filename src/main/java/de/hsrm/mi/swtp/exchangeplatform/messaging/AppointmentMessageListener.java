package de.hsrm.mi.swtp.exchangeplatform.messaging;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AppointmentMessageListener implements MessageListener {

    final String TOPICNAME = "AppointmentTopic";
    ActiveMQTopic activeMQTopic = new ActiveMQTopic(TOPICNAME);

    @NonFinal
    JmsTemplate jmsTemplate;

    @JmsListener(
            destination = TOPICNAME,
            containerFactory = "myFactory"
    )
    public void onReceiveMessage(String message) {
        log.info("========== Received <" + message + ">");
        System.out.println("========== Received <" + message + ">");
    }

    @Override
    public void onMessage(Message message) {

    }
}
