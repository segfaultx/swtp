package de.hsrm.mi.swtp.exchangeplatform.exceptions;

import de.hsrm.mi.swtp.exchangeplatform.model.data.UserModel;

public class UserIsAlreadyAttendeeException extends RuntimeException {
	
	public UserIsAlreadyAttendeeException(UserModel user) {
		super(String.format("User with id %s is already an attendee.", user.getId()));
	}
}
