package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "diagnoses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "icd10_code", length = 20)
    private String icd10Code;

    @Column(name = "diagnosis_name", nullable = false, length = 255)
    private String diagnosisName;

    @Column(name = "diagnosis_date", nullable = false)
    private LocalDate diagnosisDate;

    @Column(name = "diagnosing_doctor_id", nullable = false)
    private Long diagnosingDoctorId;

    @Column(nullable = false, length = 50)
    private String status; // PROVISIONAL, CONFIRMED, RULED_OUT

    @Column(name = "encounter_type", length = 50)
    private String encounterType;

    @Column(name = "encounter_id")
    private Long encounterId;
}
