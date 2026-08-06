package com.healthcare.clinic.pharmacy.exception;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

public class ExpiredStockException extends RuntimeException {
    public ExpiredStockException(String message) {
        super(message);
    }
}
