package com.healthcare.clinic.doctor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrescriptionItemRequest {
    @NotBlank(message = "Medication name is required")
    private String medicationName;

    @NotBlank(message = "Dosage is required")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    @NotBlank(message = "Duration is required")
    private String duration;

    private String type;

    private String instructions;

    private String strength;

    private String timing;

    private Long medicineId;

    private Integer prescribedQuantity;
}
