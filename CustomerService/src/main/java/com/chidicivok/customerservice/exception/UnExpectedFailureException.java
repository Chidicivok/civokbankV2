package com.chidicivok.customerservice.exception;

public class UnExpectedFailureException extends RuntimeException {
    public UnExpectedFailureException(String message) {
        super(message);
    }
}
