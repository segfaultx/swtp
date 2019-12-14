package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.UserModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.Connection;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserConnection {
	
	String jwtToken;
	UserModel user;
	ActiveMQQueue personalQueue;
	Connection connection;
	
	@Builder
	public UserConnection(String jwtToken, UserModel user, Connection connection, ActiveMQQueue personalQueue) {
		this.jwtToken = jwtToken;
		this.user = user;
		this.connection = connection;
		this.personalQueue = personalQueue;
	}
}
