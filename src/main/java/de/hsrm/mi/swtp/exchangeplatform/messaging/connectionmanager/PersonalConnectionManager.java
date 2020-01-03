package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.messaging.PersonalConnection;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.Map;

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
	SingleConnectionFactory connectionFactory;
	
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
		if(userConnectionMap.containsKey(queueName)) return userConnectionMap.get(queueName).getPersonalQueue();
		
		final Connection connection = connectionFactory.createQueueConnection();
		final Session session = connection.createSession();
		final ActiveMQQueue queue = (ActiveMQQueue) session.createQueue(queueName);
		final PersonalConnection personalConnection = PersonalConnection.builder()
																		.connection(connection)
																		.personalQueue(queue)
																		.user(user)
																		.build();
		queue.createDestination(createPersonalQueueDestinationName(user));
		connection.start();
		userConnectionMap.put(queueName, personalConnection);
		return queue;
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
	private String createPersonalQueueName(final User user) {
		final String FORMAT = "usr:%s-%s";
		Long userId = user.getUserType().getType().equals(TypeOfUsers.STUDENT) ? user.getStudentNumber() : user.getStaffNumber();
		String username = user.getAuthenticationInformation().getUsername();
		return String.format(FORMAT, userId, username);
	}
	
	private String createPersonalQueueDestinationName(final User user) {
		final String FORMAT = "dst:%s-%s";
		Long userId = user.getUserType().getType().equals(TypeOfUsers.STUDENT) ? user.getStudentNumber() : user.getStaffNumber();
		String username = user.getAuthenticationInformation().getUsername();
		return String.format(FORMAT, userId, username);
	}
	
	public ActiveMQQueue getConnection(User user) {
		final String queueName = createPersonalQueueName(user);
		if(!this.userConnectionMap.containsKey(queueName)) return null;
		return this.userConnectionMap.get(queueName).getPersonalQueue();
	}
	
}
