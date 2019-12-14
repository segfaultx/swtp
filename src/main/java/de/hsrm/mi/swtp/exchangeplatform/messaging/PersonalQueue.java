package de.hsrm.mi.swtp.exchangeplatform.messaging;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.apache.activemq.command.ActiveMQQueue;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonalQueue {
	
	String jwtToken;
	String username;
	ActiveMQQueue activeMQQueue;
	
	@Builder
	public PersonalQueue(String jwtToken, String username, ActiveMQQueue activeMQQueue) {
		this.jwtToken = jwtToken;
		this.username = username;
		this.activeMQQueue = activeMQQueue;
	}
}
