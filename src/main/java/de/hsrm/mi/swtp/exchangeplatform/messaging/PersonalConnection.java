package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.QueueSession;

/**
 * A class holding information about a logged in/an online {@link User}.
 * Contains information such as the user itself and the {@link Connection} in which its {@link ActiveMQQueue} is active.
 * <p>
 * This object is mainly used for management purposes {@link de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalConnectionManager}.
 */
@Builder
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonalConnection {
	
	User user;
	ActiveMQQueue personalQueue;
	MessageProducer messageProducer;
	QueueSession session;
	Connection connection;
}
