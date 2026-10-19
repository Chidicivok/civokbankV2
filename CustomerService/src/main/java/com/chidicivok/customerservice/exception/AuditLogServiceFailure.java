package com.chidicivok.customerservice.exception;

public class AuditLogServiceFailure extends RuntimeException {
    public AuditLogServiceFailure(String message) {
        super(message);
    }
}
