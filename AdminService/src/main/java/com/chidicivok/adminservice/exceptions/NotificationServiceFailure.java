package com.chidicivok.adminservice.exceptions;

public class NotificationServiceFailure extends RuntimeException {
    public NotificationServiceFailure(String message) {
        super(message);
    }
}
