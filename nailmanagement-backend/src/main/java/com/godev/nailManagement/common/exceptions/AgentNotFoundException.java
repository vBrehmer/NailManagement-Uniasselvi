package com.godev.nailManagement.common.exceptions;

public class AgentNotFoundException extends RuntimeException {
    public AgentNotFoundException(String message) {
        super(message);
    }
}
