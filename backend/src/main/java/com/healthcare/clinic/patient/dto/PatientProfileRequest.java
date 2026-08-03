package com.healthcare.clinic.patient.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PatientProfileRequest {
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    
    @jakarta.validation.constraints.NotBlank
    private String emergencyContactName;
    
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String emergencyContactPhone;
    
    private String address;
    private String medicalHistorySummary;
    private Long branchId;
    private String allergies;
    private String insuranceStatus;
    private String injuryStatus;
}
