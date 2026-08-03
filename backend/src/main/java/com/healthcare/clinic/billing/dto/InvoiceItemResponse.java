package com.healthcare.clinic.billing.dto;

import com.healthcare.clinic.billing.entity.ItemType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceItemResponse {
    private Long id;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private ItemType itemType;
    private Long referenceId;
}
