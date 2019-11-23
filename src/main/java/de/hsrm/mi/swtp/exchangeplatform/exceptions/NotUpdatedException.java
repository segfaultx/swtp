package de.hsrm.mi.swtp.exchangeplatform.exceptions;

public class NotUpdatedException extends RuntimeException {

    public NotUpdatedException() {
        this("Not updated.");
    }

    public NotUpdatedException(String message) {
        super(message);
    }
}
