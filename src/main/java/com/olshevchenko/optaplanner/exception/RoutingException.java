package com.olshevchenko.optaplanner.exception;

public class RoutingException extends RuntimeException {

    public RoutingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoutingException(String message) {
        super(message);
    }
}
