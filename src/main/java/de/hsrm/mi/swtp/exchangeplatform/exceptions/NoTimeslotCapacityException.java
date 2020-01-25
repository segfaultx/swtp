package de.hsrm.mi.swtp.exchangeplatform.exceptions;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;

public class NoTimeslotCapacityException extends RuntimeException {
	
	public NoTimeslotCapacityException(Timeslot timeslot) {
		super(String.format("There is no more capacity available in given timeslot.\nTimeslot: %s", timeslot));
	}
}
