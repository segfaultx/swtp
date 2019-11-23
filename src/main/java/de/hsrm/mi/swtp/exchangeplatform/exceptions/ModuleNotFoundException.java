package de.hsrm.mi.swtp.exchangeplatform.exceptions;

public class ModuleNotFoundException extends NotFoundException {

    public ModuleNotFoundException() {
        super("Could not find the requested module.");
    }

    public ModuleNotFoundException(String message) {
        super(message);
    }

    public ModuleNotFoundException(Long appointmentId) {
        super(String.format(
                "Could not find the requested module: id=%s.",
                appointmentId
        ));
    }

}
