package de.hsrm.mi.swtp.exchangeplatform.exceptions;

public class StudentNotUpdatedException extends NotUpdatedException {

    public StudentNotUpdatedException() {
        this("Student not updated.");
    }

    public StudentNotUpdatedException(String message) {
        super(message);
    }
}
