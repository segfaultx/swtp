package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@Builder
public class StudentMessageSender implements MessageSender<Student> {

    JmsTemplate jmsTemplate;
    ActiveMQQueue studentQueue;

    @Override
    public void send() {
        log.info("STUDENT WURDE ABGERUFEN");
        this.send("STUDENT WURDE ABGERUFEN");
    }

    @Override
    public void send(Student model) {
        String message = String.format(
                "Create Student-Event TextMessage::< STUDENT {} WURDE ABGERUFEN/-GEÄNDERT. >",
                model.getMatriculationNumber()
        );
        this.send(message);
    }

    @Override
    public void send(String message) {
        log.info(message);
        jmsTemplate.send(studentQueue, session -> session.createTextMessage(message));
    }

}
