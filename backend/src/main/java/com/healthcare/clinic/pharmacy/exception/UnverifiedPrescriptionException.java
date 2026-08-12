package com.healthcare.clinic.pharmacy.exception;


public class UnverifiedPrescriptionException extends RuntimeException {
    public UnverifiedPrescriptionException(String message) {
        super(message);
    }
}
