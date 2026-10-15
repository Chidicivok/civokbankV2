package com.chidicivok.adminservice.exceptions;

public class AuditLogServiceFailure extends RuntimeException {
    public AuditLogServiceFailure(String message) {
        super(message);
    }
}
