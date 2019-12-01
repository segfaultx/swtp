package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@EnableJms
@RequiredArgsConstructor
public class TimeslotMessageListener implements MessageListener {

    public final static String TOPICNAME = "TimeslotTopic";
    public final static String QUEUENAME = "TimeslotQueue";
    ActiveMQTopic activeMQTopic = new ActiveMQTopic(TOPICNAME);

    JmsTemplate timeslotJmsTemplate;

    @JmsListener(
            destination = TOPICNAME,
            containerFactory = "timeslotTopicFactory"
    )
    public void onReceiveMessage(String message) {
        log.info("Received <" + message + ">");
    }

    @JmsListener(
            destination = QUEUENAME,
            containerFactory = "timeslotQueueFactory"
    )
    public void onReceiveAoppointmentMessage(Timeslot timeslot) {
    }

    @Override
    public void onMessage(Message message) {
        log.info("Es kam ein neuer Termin rein: " + message.toString());
        timeslotJmsTemplate.send(activeMQTopic, session -> message);
    }
}
