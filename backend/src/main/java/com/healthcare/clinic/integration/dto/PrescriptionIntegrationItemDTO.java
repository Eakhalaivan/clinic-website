package com.healthcare.clinic.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionIntegrationItemDTO {
    private String medicationName;
    private String type;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
    private String strength;
    private String timing;
    private Long medicineId;
    private Integer prescribedQuantity;
    private Integer dispensedQuantity;
    private Integer remainingQuantity;
}
