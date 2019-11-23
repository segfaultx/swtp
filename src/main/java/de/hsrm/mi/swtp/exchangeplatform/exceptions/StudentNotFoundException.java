package de.hsrm.mi.swtp.exchangeplatform.exceptions;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentNotFoundException extends NotFoundException {

    public StudentNotFoundException() {
        super("Could not find the requested student.");
    }

    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(Long matriculationNumber) {
        super(String.format(
                "Could not find the requested student: matriculationNumber=%s.",
                matriculationNumber
        ));
    }

}
