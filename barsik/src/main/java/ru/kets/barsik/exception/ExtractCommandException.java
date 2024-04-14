package ru.kets.barsik.exception;

import java.util.function.Supplier;

public class ExtractCommandException extends Exception {
    public ExtractCommandException(String message) {
        super(message);
    }

    public ExtractCommandException() {
        super();
    }
}
