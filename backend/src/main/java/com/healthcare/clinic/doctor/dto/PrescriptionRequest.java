package com.healthcare.clinic.doctor.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long appointmentId;

    private String notes;

    private String chiefComplaint;

    private String diagnosis;

    private String symptoms;

    private String medicalHistory;

    private java.time.LocalDateTime followUpDate;

    private List<Long> labTestCatalogIds;

    @NotEmpty(message = "At least one medication is required")
    @Valid
    private List<PrescriptionItemRequest> items;
}
