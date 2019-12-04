package de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentNotCreatedException extends NotCreatedException {
	
	public AppointmentNotCreatedException(Timeslot timeslot) {
		super(timeslot);
	}
	
}
