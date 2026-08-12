package com.healthcare.clinic.billing.entity;

public enum InvoiceStatus {
    DRAFT,
    ISSUED,
    PARTIALLY_PAID,
    PAID,
    CANCELLED,
    CREDIT_NOTED,
    REFUNDED,
    WRITTEN_OFF
}
