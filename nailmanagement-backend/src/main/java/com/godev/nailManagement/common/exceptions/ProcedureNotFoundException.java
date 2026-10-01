package com.godev.nailManagement.common.exceptions;

public class ProcedureNotFoundException extends RuntimeException {
    public ProcedureNotFoundException(String message) {
        super(message);
    }
}
