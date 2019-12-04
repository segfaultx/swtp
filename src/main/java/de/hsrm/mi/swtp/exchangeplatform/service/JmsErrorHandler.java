package de.hsrm.mi.swtp.exchangeplatform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

/**
 * An ErrorHandler class specifically for handling error concerning JMS-messaging.
 */
@Slf4j
@Service
public class JmsErrorHandler implements ErrorHandler {
	
	@Override
	public void handleError(Throwable t) {
		log.warn("In default jms error handler...");
		log.error("Error Message : {}", t.getMessage());
	}
}
