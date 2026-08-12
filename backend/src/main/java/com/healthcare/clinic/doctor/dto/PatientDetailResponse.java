package com.healthcare.clinic.doctor.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PatientDetailResponse {
    private Long patientId; // userId
    private Long profileId; // the actual PatientProfile id
    private String name;
    private String phone;
    private String email;
    private Integer age;
    private java.time.LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String emergencyContactName;
    private String emergencyContactPhone;

    // Medical info
    private String allergies;
    private String chronicConditions;
    private String medicalHistorySummary;
    private String pastSurgeries;
    private String familyHistory;
    private String currentMedications;
    
    // Vitals (not implemented yet, return empty)
    private List<Object> vitalsHistory;

    // Appointment history
    private List<AppointmentHistoryDto> appointmentHistory;

    // Previous Prescriptions
    private List<PreviousPrescriptionDto> previousPrescriptions;

    @Data
    @Builder
    public static class AppointmentHistoryDto {
        private Long appointmentId;
        private String date;
        private String reason;
        private String status;
        private String notes;
        private Long prescriptionId; // if we want to link it
    }

    @Data
    @Builder
    public static class PreviousPrescriptionDto {
        private Long id;
        private String date;
        private String doctorName;
        private Integer itemCount;
        private String summary;
    }
}
