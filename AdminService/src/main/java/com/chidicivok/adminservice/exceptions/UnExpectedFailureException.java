package com.chidicivok.adminservice.exceptions;

public class UnExpectedFailureException extends RuntimeException {
    public UnExpectedFailureException(String message) {
        super(message);
    }
}
