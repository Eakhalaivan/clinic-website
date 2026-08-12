package com.healthcare.clinic.pharmacy.exception;


public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
