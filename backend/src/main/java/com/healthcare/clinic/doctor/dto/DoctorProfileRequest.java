package com.healthcare.clinic.doctor.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DoctorProfileRequest {
    @jakarta.validation.constraints.NotBlank
    private String specialty;
    
    @jakarta.validation.constraints.NotBlank
    private String qualifications;
    
    private Integer experienceYears;
    
    @jakarta.validation.constraints.NotNull
    @jakarta.validation.constraints.Min(0)
    private BigDecimal consultationFee;
    
    private String bio;
    private Boolean isActive = true;
    private Long branchId;
}
