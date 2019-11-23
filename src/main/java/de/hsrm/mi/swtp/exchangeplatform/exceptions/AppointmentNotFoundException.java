package de.hsrm.mi.swtp.exchangeplatform.exceptions;

public class AppointmentNotFoundException extends NotFoundException {

    public AppointmentNotFoundException() {
        super("Could not find the requested appointment.");
    }

    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException(Long appointmentId) {
        super(String.format(
                "Could not find the requested appointment: id=%s.",
                appointmentId
        ));
    }

}
