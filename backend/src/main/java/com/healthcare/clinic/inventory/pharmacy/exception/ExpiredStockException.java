package com.healthcare.clinic.inventory.pharmacy.exception;

public class ExpiredStockException extends RuntimeException {
    public ExpiredStockException(String message) {
        super(message);
    }
}
