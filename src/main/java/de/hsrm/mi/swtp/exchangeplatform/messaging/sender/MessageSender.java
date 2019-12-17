package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;

/**
 * An interface with basic methods for sending JMS-messages.
 *
 * @param <T> a class which has to extend a {@link Model}.
 */
public interface MessageSender<T> {
	
	void send();
	
	/**
	 * Used to send a (simple) {@link Model} instance.
	 *
	 * @param model the object which is to be sent via a {@link javax.jms.Topic} or {@link javax.jms.Queue}.
	 */
	void send(T model);
	
	/**
	 * Used to send a (simple) message.
	 *
	 * @param message the message which is to be sent via a {@link javax.jms.Topic} or {@link javax.jms.Queue}.
	 */
	void send(String message);
	
}
