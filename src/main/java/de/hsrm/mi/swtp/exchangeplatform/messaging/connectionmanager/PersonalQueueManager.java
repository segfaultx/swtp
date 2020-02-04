package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.dynamicdestination.PersonalQueue;
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
 * Manages {@link PersonalQueue personal queues}.
 * Can create a {@link Queue} wrapped inside a {@link PersonalQueue} for each logged {@link User} and close those when the user goes offline - and of course manages them.
 *
 * @see DestinationManager
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class PersonalQueueManager implements DestinationManager {
	final String FORMAT = "usr:%s-%s";
	
	Map<String, PersonalQueue> personalQueueMap;
	ActiveMQConnectionFactory connectionFactory;
	
	/**
	 * Creates a new connection with a session and {@link ActiveMQQueue} for the logged in {@link User}.
	 *
	 * @param user is the logged in user itself.
	 *
	 * @return the dynamically created {@link ActiveMQQueue} over which the server can communicate specific changes
	 * to the user.
	 */
	public ActiveMQQueue createPersonalQueue(final User user) throws JMSException {
		final String queueName = createPersonalQueueName(user);
		if(queueName == null) return null;
		if(personalQueueMap.containsKey(queueName)) return personalQueueMap.get(queueName).getPersonalQueue();
		
		final String username = user.getAuthenticationInformation().getPassword();
		QueueConnection connection = connectionFactory.createQueueConnection(username, username);
		final QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		final ActiveMQQueue queue = (ActiveMQQueue) session.createQueue(queueName);
		
		final QueueSender messageProducer = session.createSender(queue);
		final PersonalQueue personalQueue = PersonalQueue.builder()
														 .connection(connection)
														 .session(session)
														 .personalQueue(queue)
														 .messageProducer(messageProducer)
														 .user(user)
														 .build();
		connection.start();
		personalQueueMap.put(queueName, personalQueue);
		
		return personalQueue.getPersonalQueue();
	}
	
	/** Creates a queue for an offline client. */
	public ActiveMQQueue createQueueForOfflineUser(final User user) throws JMSException {
		// TODO: manage queues for offline clients
		final String queueName = createPersonalQueueName(user);
		final String username = user.getAuthenticationInformation().getPassword();
		QueueConnection connection = connectionFactory.createQueueConnection(username, username);
		final QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		return (ActiveMQQueue) session.createQueue(queueName);
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
		PersonalQueue personalQueue = this.personalQueueMap.remove(createPersonalQueueName(user));
		if(personalQueue == null) return false;
		personalQueue.getConnection().close();
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
		
		Long userId = user.getUserType().getType().equals(TypeOfUsers.STUDENT) ? user.getStudentNumber() : user.getStaffNumber();
		String username = user.getAuthenticationInformation().getUsername();
		return String.format(FORMAT, userId, username);
	}
	
	public PersonalQueue getPersonalQueue(User user) {
		final String queueName = createPersonalQueueName(user);
		if(!this.personalQueueMap.containsKey(queueName)) return null;
		return this.personalQueueMap.get(queueName);
	}
	
	public ActiveMQQueue getPersonalQueue(User user, Boolean ifOfflineCreateTemp) {
		ActiveMQQueue queue = this.getQueue(user);
		try {
			if(ifOfflineCreateTemp && queue == null) return this.createQueueForOfflineUser(user);
		} catch(JMSException e) {
			return null;
		}
		return queue;
	}
	
	public ActiveMQQueue getQueue(User user) {
		final String queueName = createPersonalQueueName(user);
		if(!this.personalQueueMap.containsKey(queueName)) return null;
		return this.personalQueueMap.get(queueName).getPersonalQueue();
	}
	
	public ActiveMQQueue getQueue(Long userId) {
		try {
			return this.getQueue(this.getUserById(userId));
		} catch(NotFoundException e) {
			return null;
		}
	}
	
	/**
	 * Sends a message to the dynamically created {@link Queue} of a {@link User}.
	 * If the {@link User} has no {@link PersonalQueue} in the {@link #personalQueueMap} nothing happens.
	 *
	 * @param user    {@link User}
	 * @param message the message which is to be sent to the {@link User}
	 */
	public void send(User user, String message) throws JMSException {
		PersonalQueue personalQueue = this.personalQueueMap.get(createPersonalQueueName(user));
		if(personalQueue == null) return;
		personalQueue.getMessageProducer().send(personalQueue.getSession().createTextMessage(message));
	}
	
	private User getUserById(Long id) throws NotFoundException {
		Optional<PersonalQueue> userOpt = this.personalQueueMap.values()
															   .stream()
															   .filter(personalQueue -> personalQueue.getUser().getId().equals(id))
															   .findFirst();
		if(userOpt.isEmpty()) throw new NotFoundException();
		return userOpt.get().getUser();
	}
	
}
