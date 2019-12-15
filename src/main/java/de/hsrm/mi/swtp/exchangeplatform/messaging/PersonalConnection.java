package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.UserModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.Connection;

/**
 * A class holding information about a logged in/an online {@link UserModel}.
 * Contains information such as the user itself and the {@link Connection} in which its {@link ActiveMQQueue} is active.
 * <p>
 * This object is mainly used for management purposes {@link de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalConnectionManager}.
 */
@Builder
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonalConnection {
	
	UserModel user;
	ActiveMQQueue personalQueue;
	Connection connection;
}
