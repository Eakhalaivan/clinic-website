package com.healthcare.clinic.medicalrecord.dto;

import com.healthcare.clinic.medicalrecord.entity.RecordType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MedicalRecordResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String doctorName; // Optionally added by service
    private RecordType recordType;
    private String title;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
