package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class PersonalQueueManager {
	
	JmsTemplate personalJmsTemplate;
	Map<String, PersonalQueue> personalQueues;
	ActiveMQConnectionFactory activeMQConnectionFactory;
	
	public ActiveMQQueue createNewQueue(final String jwtToken, final User user) {
		if(personalQueues.containsKey(jwtToken)) return personalQueues.get(jwtToken).getActiveMQQueue();
		
		final String queueName = createPersonalQueueName(user);
		final ActiveMQQueue amqQueue = new ActiveMQQueue(queueName);
		PersonalQueue personalQueue = PersonalQueue.builder()
												   .jwtToken(jwtToken)
												   .activeMQQueue(amqQueue)
												   .username(user.getFirstName())
												   .build();
		personalQueues.put(jwtToken, personalQueue);
		return amqQueue;
	}
	
	public void closeQueue(final String jwtToken) {
		if(!personalQueues.containsKey(jwtToken)) return;
		
		final PersonalQueue personalQueue = this.personalQueues.get(jwtToken);
	}
	
	private String createPersonalQueueName(final User user) {
		final String FORMAT = "amq:usr:%s-%s";
		Long userId = user.getUserType().getType().equals(TypeOfUsers.STUDENT)
				? user.getStudentNumber()
				: user.getStaffNumber();
		UUID uuid = UUID.fromString(user.getStudentNumber() + user.getEmail());
		return String.format(FORMAT, userId, uuid.toString());
	}
	
}
