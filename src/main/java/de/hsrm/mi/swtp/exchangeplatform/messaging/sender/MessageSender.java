package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

/**
 * An interface with basic methods for sending JMS-messages.
 */
@Component
public interface MessageSender {
	
	/**
	 * Used to send a (simple) message.
	 *
	 * @param message the message which is to be sent via a {@link javax.jms.Topic} or {@link javax.jms.Queue}.
	 */
	void send(final MessageProducer messageProducer, Message message) throws JMSException;
	
}
