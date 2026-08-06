package com.healthcare.clinic.pharmacy.exception;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public class UnverifiedPrescriptionException extends RuntimeException {
    public UnverifiedPrescriptionException(String message) {
        super(message);
    }
}
