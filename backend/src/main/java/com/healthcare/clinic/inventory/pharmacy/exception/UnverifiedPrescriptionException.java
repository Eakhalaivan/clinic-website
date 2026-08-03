package com.healthcare.clinic.inventory.pharmacy.exception;

public class UnverifiedPrescriptionException extends RuntimeException {
    public UnverifiedPrescriptionException(String message) {
        super(message);
    }
}
