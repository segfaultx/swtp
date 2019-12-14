package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.model.data.UserModel;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import de.hsrm.mi.swtp.exchangeplatform.service.authentication.JWTTokenUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Map;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class PersonalQueueManager {
	
	JmsTemplate jmsTemplate;
	Map<String, UserConnection> userConnectionMap;
	SingleConnectionFactory connectionFactory;
	ActiveMQQueue studentQueue;
	
	public ActiveMQQueue createNewConnection(final String jwtToken, final UserModel user) throws JMSException {
		if(userConnectionMap.containsKey(jwtToken)) return userConnectionMap.get(jwtToken).getPersonalQueue();
		
		final Long clientId = user.getId();
		final String queueName = createPersonalQueueName(user);
		
		final Connection connection = connectionFactory.createQueueConnection();
		final Session session = connection.createSession();
		session.setMessageListener(message -> {
			log.info("USER CONNECTION MESSAGE");
			try {
				log.info(">>>>>> " + ((TextMessage) message).getText());
			} catch(JMSException e) {
				log.info(">>>>>> " + message.toString());
			}
		});
		session.createConsumer(studentQueue);
		connection.start();
		
		final ActiveMQQueue queue = (ActiveMQQueue) session.createQueue(queueName);
		session.createProducer(queue);
		UserConnection userConnection = UserConnection.builder()
													  .connection(connection)
													  .jwtToken(jwtToken)
													  .personalQueue(queue)
													  .user(user)
													  .build();
		
		userConnectionMap.put(jwtToken, userConnection);
		return queue;
	}
	
	public void closeConnection(final String jwtToken) throws JMSException {
		if(!JWTTokenUtils.isValidToken(jwtToken)) return;
		UserConnection userConnection = this.userConnectionMap.remove(jwtToken);
		if(userConnection == null) return;
		userConnection.getConnection().close();
	}
	
	private String createPersonalQueueName(final UserModel user) {
		final String FORMAT = "amq:usr:%s-%s";
		Long userId = user.getUserType().getType().equals(TypeOfUsers.STUDENT) ? user.getStudentNumber() : user.getStaffNumber();
		String hash = user.getStudentNumber() + user.getEmail();
		return String.format(FORMAT, userId, hash);
	}
	
}
