package com.healthcare.clinic.inpatient.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private Long newBedId;
    private String reason;
}
