package com.oskin.autoservice.exception;

public class NoValidUserException extends RuntimeException {
    public NoValidUserException(String message) {
        super(message);
    }
}
