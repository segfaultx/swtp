package de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	
	public NotFoundException() {
		super("Could not find the requested object.");
	}
	
	public NotFoundException(String message) {
		super(message);
	}
	
	public NotFoundException(Long id) {
		super(String.format("Could not find the requested object by id '%s'.", id));
	}
	
}
