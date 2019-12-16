package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

/**
 * An interface with basic methods for sending JMS-messages.
 */
public interface MessageSender<T> {
	
	void send();
	
	/**
	 * Used to send a (simple) object instance.
	 *
	 * @param object the object which is to be sent via a {@link javax.jms.Topic} or {@link javax.jms.Queue}.
	 */
	void send(T object);
	
	/**
	 * Used to send a (simple) message.
	 *
	 * @param message the message which is to be sent via a {@link javax.jms.Topic} or {@link javax.jms.Queue}.
	 */
	void send(String message);
	
}
