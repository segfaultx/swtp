package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@EnableJms
public class AppointmentMessageListener {
    
    final String TOPICNAME = "AppointmentTopic";
    final String QUEUENAME = "AppointmentQueue";
    ActiveMQTopic activeMQTopic = new ActiveMQTopic(TOPICNAME);
    
    @Autowired
    @NonFinal
    private JmsTemplate jmsTemplate;
    
    @JmsListener(
            destination = TOPICNAME,
            containerFactory = "myTopicFactory"
    )
    public void onReceiveMessage(String message) {
        log.info("Received <" + message + ">");
    }
    
    @JmsListener(
            destination = QUEUENAME,
            containerFactory = "myQueueFactory"
    )
    public void onReceiveAoppointmentMessage(Appointment appointment) {
        log.info("Es kam ein neuer Termin rein: " + appointment.toString());
        jmsTemplate.send(activeMQTopic, session -> session.createTextMessage("Es kam ein neuer Termin rein: " + appointment.toString()));
    }
    
}
