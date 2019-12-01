package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@Builder
public class TimeslotMessageSender implements MessageSender<Timeslot> {

    JmsTemplate jmsTemplate;

    @Qualifier("timeslotDestination")
    Destination timeslotDestination;

    @Override
    public void send() {
        log.info("Create Timeslot-Event TextMessage::< TIMESLOT WURDE ABGERUFEN/-GEÄNDERT. >");
        jmsTemplate.send(timeslotDestination, session -> session.createTextMessage("TIMESLOT WURDE ABGERUFEN"));
    }

    @Override
    public void send(Timeslot model) {
        log.info(String.format(
                "Create Timeslot-Event TextMessage::< TIMESLOT {} WURDE ABGERUFEN/-GEÄNDERT. >",
                model.getId()
        ));
        jmsTemplate.send(timeslotDestination, session -> session.createTextMessage(String.format(
                "TIMESLOT {} WURDE ABGERUFEN",
                model.getId()
        )));
    }

}
