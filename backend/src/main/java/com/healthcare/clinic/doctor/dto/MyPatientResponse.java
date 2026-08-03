package com.healthcare.clinic.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPatientResponse {
    private Long id;
    private Long patientId; // The user ID
    private String name;
    private String phone;
    private String bloodGroup;
    private String gender;
    private Integer age;
    private LocalDateTime lastVisitDate;
    private LocalDateTime upcomingAppointmentDate;
    private String status; // Active / Inactive
}
