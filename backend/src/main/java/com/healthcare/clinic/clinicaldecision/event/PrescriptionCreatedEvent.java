package com.healthcare.clinic.clinicaldecision.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionCreatedEvent {
    private Long patientId;
    private Long prescriptionId;
    private List<String> medicationNames;
    private Long doctorId;
}
