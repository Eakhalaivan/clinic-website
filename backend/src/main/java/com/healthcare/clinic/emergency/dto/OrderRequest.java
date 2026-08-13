package com.healthcare.clinic.emergency.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String orderType;
    private Long referenceId;
}
