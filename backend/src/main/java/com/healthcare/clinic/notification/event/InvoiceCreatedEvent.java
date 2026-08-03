package com.healthcare.clinic.notification.event;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InvoiceCreatedEvent {
    private Long invoiceId;
    private Long patientId;
    private String patientEmail;
    private String patientName;
    private String invoiceNumber;
    private BigDecimal totalAmount;
    private LocalDateTime dueDate;
}
