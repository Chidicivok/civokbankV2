package com.chidicivok.accountservices.exception;

public class CustomerClientServiceFailure extends RuntimeException {
    public CustomerClientServiceFailure(String message) {
        super(message);
    }
}
