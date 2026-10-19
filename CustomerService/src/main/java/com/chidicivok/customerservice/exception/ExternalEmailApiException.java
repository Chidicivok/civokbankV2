package com.chidicivok.customerservice.exception;

public class ExternalEmailApiException extends RuntimeException {
    public ExternalEmailApiException(String message) {
        super(message);
    }
}
