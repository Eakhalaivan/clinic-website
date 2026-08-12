package com.healthcare.clinic.notification.event;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class QueueTokenCalledEvent {
    private Long tokenId;
    private Long branchId;
    private String branchName;
    private int tokenNumber;
    private Long patientUserId;
    private String patientEmail; // optional — only if linked to a walk-in with email
}
