package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
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
public class ModuleMessageSender implements MessageSender<Module> {

    JmsTemplate jmsTemplate;
    ActiveMQQueue timeslotQueue;

    @Override
    public void send() {
        log.info("MODULE WURDE ABGERUFEN");
        this.send("MODULE WURDE ABGERUFEN");
    }

    @Override
    public void send(Module model) {
        String message = String.format(
                "Create Module-Event TextMessage::< MODULE {} WURDE ABGERUFEN/-GEÄNDERT. >",
                model.getName()
        );
        this.send(message);
    }

    @Override
    public void send(String message) {
        log.info(message);
        jmsTemplate.send(timeslotQueue, session -> session.createTextMessage(message));
    }

}
