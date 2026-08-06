package com.healthcare.clinic.pharmacy.entity;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;


import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "prescriptions_dispensed")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDispensed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prescription_id", nullable = false)
    private Long prescriptionId;

    @Column(name = "pharmacist_id", nullable = false)
    private Long pharmacistId;

    @Column(name = "dispensed_at", updatable = false)
    @Builder.Default
    private ZonedDateTime dispensedAt = ZonedDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String notes;
}
