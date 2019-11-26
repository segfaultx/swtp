package de.hsrm.mi.swtp.exchangeplatform.exceptions;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;

public class NoAppointmentCapacityException extends RuntimeException {

    public NoAppointmentCapacityException() {
        super("There is no more capacity available in given appointment.");
    }

    public NoAppointmentCapacityException(Appointment appointment) {
        super(String.format(
                "There is no more capacity available in given appointment.\nAppointment: %s",
                appointment
        ));
    }
}
