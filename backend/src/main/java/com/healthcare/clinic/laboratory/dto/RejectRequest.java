package com.healthcare.clinic.laboratory.dto;

import com.healthcare.clinic.laboratory.entity.RejectionReason;
import lombok.Data;

@Data
public class RejectRequest {
    private RejectionReason reason;
    private String notes;
}
