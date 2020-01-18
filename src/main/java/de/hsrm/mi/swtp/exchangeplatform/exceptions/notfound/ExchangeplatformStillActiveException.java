package de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExchangeplatformStillActiveException extends ResponseStatusException {
	
	public static final String REASON = "The exchangeplatform is still active. It needs to be deactivated first before making any changes to the PO.";
	
	public ExchangeplatformStillActiveException() {
		super(HttpStatus.CONFLICT, REASON);
	}
}
