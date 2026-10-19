package com.chidicivok.customerservice.exception;

public class NotificationServiceFailure extends RuntimeException {
    public NotificationServiceFailure(String message) {
        super(message);
    }
}
