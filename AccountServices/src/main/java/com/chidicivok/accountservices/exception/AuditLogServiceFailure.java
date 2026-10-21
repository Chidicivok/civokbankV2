package com.chidicivok.accountservices.exception;

public class AuditLogServiceFailure extends RuntimeException {
    public AuditLogServiceFailure(String message) {
        super(message);
    }
}
