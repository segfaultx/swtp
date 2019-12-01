package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
@RequiredArgsConstructor
public class StudentMessageListener {

    public final static String TOPICNAME = "StudentTopic";
    public final static String QUEUENAME = "StudentQueue";
    ActiveMQTopic activeMQTopic = new ActiveMQTopic(TOPICNAME);

    @Autowired
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
    public void onReceiveStudentMessage(Student student) {
        log.info("Änderung Student: " + student.toString());
        jmsTemplate.send(activeMQTopic, session -> session.createTextMessage("Änderung an Student kam rein: " + student.toString()));
        jmsTemplate.convertAndSend(TOPICNAME, student);
    }

}
