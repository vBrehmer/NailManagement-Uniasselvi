package com.godev.nailManagement.common.exceptions;

public class AgendaNotFoundException extends RuntimeException {
    public AgendaNotFoundException(String message) {
        super(message);
    }
}
