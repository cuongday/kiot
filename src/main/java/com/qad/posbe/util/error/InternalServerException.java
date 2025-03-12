package com.qad.posbe.util.error;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
} 