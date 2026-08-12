package com.healthcare.clinic.laboratory.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabStatusChangedEvent {
    private Long labRequestId;
    private String newStatus;
    private String previousStatus;
}
