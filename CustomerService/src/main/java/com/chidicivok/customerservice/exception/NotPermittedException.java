package com.chidicivok.customerservice.exception;

public class NotPermittedException extends RuntimeException {
    public NotPermittedException(String message) {
        super(message);
    }
}
