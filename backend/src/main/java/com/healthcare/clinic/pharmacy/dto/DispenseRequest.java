package com.healthcare.clinic.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispenseRequest {
    private Long prescriptionId;
    private String notes;
    private List<DispenseItemRequest> items;
    private String idempotencyKey;
    private boolean partialDispense;
}
