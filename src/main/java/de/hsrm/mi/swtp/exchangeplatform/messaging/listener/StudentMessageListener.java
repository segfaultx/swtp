package de.hsrm.mi.swtp.exchangeplatform.messaging.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
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
public class StudentMessageListener implements MessageListener {

    public final static String TOPICNAME = "StudentTopic";
    public final static String QUEUENAME = "StudentQueue";
    ActiveMQQueue studentQueue;

    JmsTemplate jmsTemplate;

    @JmsListener(
            destination = TOPICNAME,
            containerFactory = "studentTopicFactory"
    )
    public void onReceiveMessage(String message) {
        log.info("Received <" + message + ">");
    }

    @JmsListener(
            destination = QUEUENAME,
            containerFactory = "studentQueueFactory"
    )
    @Override
    public void onMessage(Message message) {
        try {
            log.info("Es kam ein neuer Termin rein: " + ((TextMessage) message).getText());
        } catch (JMSException e) {
            log.info("ERROR: " + message);
        }
        jmsTemplate.send(studentQueue, session -> session.createTextMessage(
                "Erhaltene interne Server-Nachricht: " + ((TextMessage) message).getText() + "\n" +
                        "Student-Änderungen erkannt. Implementierung noch nicht abgeschlossen!"
        ));
    }
}
