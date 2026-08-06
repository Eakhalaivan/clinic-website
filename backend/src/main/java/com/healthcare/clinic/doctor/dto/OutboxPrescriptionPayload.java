package com.healthcare.clinic.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxPrescriptionPayload {
    private String patientName;
    private String doctorName;
    private Long clinicalPrescriptionId;
    private Long pharmacyUserId;
    private List<OutboxPrescriptionItem> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutboxPrescriptionItem {
        private String medicationName;
        private String type;
        private String dosage;
        private String frequency;
        private String duration;
        private String instructions;
        private String strength;
        private String timing;
    }
}
