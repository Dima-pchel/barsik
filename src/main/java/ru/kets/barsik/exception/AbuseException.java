package ru.kets.barsik.exception;

public class AbuseException extends Exception {

    public AbuseException(String message) {
        super(message);
    }

    public AbuseException() {
        super();
    }
}
