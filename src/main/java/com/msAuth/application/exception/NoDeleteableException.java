package com.msAuth.application.exception;

public class NoDeleteableException extends RuntimeException {
    public NoDeleteableException(String message) {
        super(message);
    }
}
