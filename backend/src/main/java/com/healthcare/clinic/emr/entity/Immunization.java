package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "immunizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Immunization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "vaccine_name", nullable = false, length = 255)
    private String vaccineName;

    @Column(name = "dose_number", nullable = false)
    private Integer doseNumber;

    @Column(name = "administered_date", nullable = false)
    private LocalDate administeredDate;

    @Column(name = "administered_by_user_id", nullable = false)
    private Long administeredByUserId;

    @Column(name = "batch_number", length = 100)
    private String batchNumber;

    @Column(name = "next_due_date")
    private LocalDate nextDueDate;
}
