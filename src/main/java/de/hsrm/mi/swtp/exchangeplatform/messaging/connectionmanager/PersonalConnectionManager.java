package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.PersonalConnection;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.LoginSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Map;
import java.util.Optional;

/**
 * Manages {@link PersonalConnection PersonalConnections}.
 * Can create a connection for each logged {@link User} and close those when the user goes offline.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class PersonalConnectionManager {
	
	Map<String, PersonalConnection> userConnectionMap;
	ActiveMQConnectionFactory connectionFactory;
	ObjectMapper objectMapper;
	
	/**
	 * Creates a new connection with a session and {@link ActiveMQQueue} for the logged in {@link User}.
	 *
	 * @param user is the logged in user itself.
	 *
	 * @return the dynamically created {@link ActiveMQQueue} over which the server can communicate specific changes
	 * to the user.
	 */
	public ActiveMQQueue createNewConnection(final User user) throws JMSException {
		final String queueName = createPersonalQueueName(user);
		if(queueName == null) return null;
		if(userConnectionMap.containsKey(queueName)) return userConnectionMap.get(queueName).getPersonalQueue();
		
		final String username = user.getAuthenticationInformation().getPassword();
		QueueConnection connection = connectionFactory.createQueueConnection(username, username);
		final QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		final Queue queue = session.createQueue(queueName);
		final QueueSender messageProducer = session.createSender(queue);
		final PersonalConnection personalConnection = PersonalConnection.builder()
																		.connection(connection)
																		.session(session)
																		.personalQueue((ActiveMQQueue) queue)
																		.messageProducer(messageProducer)
																		.user(user)
																		.build();
		connection.start();
		userConnectionMap.put(queueName, personalConnection);
		
		QueueReceiver receiver = session.createReceiver(queue);
//		receiver.setMessageListener(message -> {
//			try {
//				log.info("\n::" + username);
//				log.info("\n::received" + ((TextMessage) message).getText());
//			} catch(JMSException e) {
//				e.printStackTrace();
//			}
//		});
		
		try {
			messageProducer.send(session.createTextMessage(objectMapper.writeValueAsString(new LoginSuccessfulMessage())));
		} catch(JsonProcessingException e) {
			messageProducer.send(session.createTextMessage(new LoginSuccessfulMessage().getMessage()));
		}
		
		return personalConnection.getPersonalQueue();
	}
	
	/**
	 * A helper method for closing an active messaging connection.
	 *
	 * @param user {@link User}
	 *
	 * @return a boolean which indicates whether the requested user has a running connection and the connection was
	 * closed successfully or not.
	 */
	public boolean closeConnection(final User user) throws JMSException {
		PersonalConnection personalConnection = this.userConnectionMap.remove(createPersonalQueueName(user));
		if(personalConnection == null) return false;
		personalConnection.getConnection().close();
		return true;
	}
	
	/**
	 * Creates and returns a string which is used for the personal queue of a logged in user.
	 *
	 * @param user {@link User}
	 */
	public String createPersonalQueueName(final User user) {
		if(user == null) return null;
		if(user.getAuthenticationInformation() == null || user.getAuthenticationInformation().getUsername() == null || user.getLastName() == null) {
			return null;
		}
		
		final String FORMAT = "usr:%s-%s";
		Long userId = user.getUserType().getType().equals(TypeOfUsers.STUDENT) ? user.getStudentNumber() : user.getStaffNumber();
		String username = user.getAuthenticationInformation().getUsername();
		return String.format(FORMAT, userId, username);
	}
	
	public PersonalConnection getPersonalConnection(User user) {
		final String queueName = createPersonalQueueName(user);
		if(!this.userConnectionMap.containsKey(queueName)) return null;
		return this.userConnectionMap.get(queueName);
	}
	
	public ActiveMQQueue getQueue(User user) {
		final String queueName = createPersonalQueueName(user);
		if(!this.userConnectionMap.containsKey(queueName)) return null;
		return this.userConnectionMap.get(queueName).getPersonalQueue();
	}
	
	public ActiveMQQueue getQueue(Long userId) {
		try {
			return this.getQueue(this.getUserById(userId));
		}
		catch(NotFoundException e) {
			return null;
		}
	}
	
	/**
	 * Sends a message to the dynamically created {@link Queue} of a {@link User}.
	 * If the {@link User} has no {@link PersonalConnection} in the {@link #userConnectionMap} nothing happens.
	 *
	 * @param user {@link User}
	 * @param message the message which is to be sent to the {@link User}
	 */
	public void send(User user, String message) throws JMSException {
		PersonalConnection personalConnection = this.userConnectionMap.get(createPersonalQueueName(user));
		if(personalConnection == null) return;
		personalConnection.getMessageProducer().send(personalConnection.getSession().createTextMessage(message));
	}
	
	private User getUserById(Long id) throws NotFoundException {
		Optional<PersonalConnection> userOpt = this.userConnectionMap.values()
																	 .stream()
																	 .filter(personalConnection -> personalConnection.getUser().getId().equals(id))
																	 .findFirst();
		if(userOpt.isEmpty()) throw new NotFoundException();
		return userOpt.get().getUser();
	}
	
}
