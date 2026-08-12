package com.healthcare.clinic.nursing.dto;

import lombok.Data;

@Data
public class ShiftHandoverRequest {
    private Long wardId;
    private Long incomingNurseId;
    private String shiftSummary;
    private String pendingTasks;
    private String criticalPatients;
}
