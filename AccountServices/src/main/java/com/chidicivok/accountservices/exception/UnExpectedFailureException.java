package com.chidicivok.accountservices.exception;

public class UnExpectedFailureException extends RuntimeException {
    public UnExpectedFailureException(String message) {
        super(message);
    }
}
