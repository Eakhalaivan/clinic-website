package com.healthcare.clinic.billing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class InvoiceRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long appointmentId;
    private Long branchId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    private BigDecimal taxAmount = BigDecimal.ZERO;
    private BigDecimal discountAmount = BigDecimal.ZERO;

    // Kept for backward compat; will be computed from items if items provided
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal amount = BigDecimal.ZERO;

    private List<InvoiceItemRequest> items = new ArrayList<>();
}
