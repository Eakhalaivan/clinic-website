package com.healthcare.clinic.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispenseItemRequest {
    private Long medicineId;
    private Integer quantity;
}
