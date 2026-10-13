package com.smartoffice.exception;

public class TechnicianNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TechnicianNotFoundException(String message) {
        super(message);
    }
}