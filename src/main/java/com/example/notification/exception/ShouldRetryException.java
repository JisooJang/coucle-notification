package com.example.notification.exception;

public class ShouldRetryException extends RuntimeException {
    public ShouldRetryException(String msg) {
        super(msg);
    }
}
