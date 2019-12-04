package de.hsrm.mi.swtp.exchangeplatform.exceptions;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;

public class NoAppointmentCapacityException extends RuntimeException {
	
	public NoAppointmentCapacityException() {
		super("There is no more capacity available in given appointment.");
	}
	
	public NoAppointmentCapacityException(Timeslot timeslot) {
		super(String.format("There is no more capacity available in given appointment.\nAppointment: %s", timeslot));
	}
}
