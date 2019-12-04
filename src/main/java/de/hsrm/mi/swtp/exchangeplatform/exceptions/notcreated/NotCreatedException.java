package de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotCreatedException extends RuntimeException {
	
	final String MESSAGE_BASE = "Entity %s not created.";
	Model model;
	
	public NotCreatedException(Model model) {
		this.model = model;
	}
	
	@Override
	public String getMessage() {
		return String.format(MESSAGE_BASE, model.toString());
	}
}
