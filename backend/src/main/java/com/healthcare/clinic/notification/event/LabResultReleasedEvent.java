package com.healthcare.clinic.notification.event;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LabResultReleasedEvent {
    private Long requestId;
    private Long patientId;
    private String patientEmail;
    private String patientName;
    private String testName;
    private Long doctorId;
}
