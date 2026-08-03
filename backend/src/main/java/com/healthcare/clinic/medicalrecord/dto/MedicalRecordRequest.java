package com.healthcare.clinic.medicalrecord.dto;

import com.healthcare.clinic.medicalrecord.entity.RecordType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalRecordRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Record type is required")
    private RecordType recordType;

    @NotBlank(message = "Title is required")
    private String title;

    private String notes;
}
