package com.healthcare.clinic.inventory.pharmacy.entity;

import com.healthcare.clinic.doctor.entity.Prescription;
import com.healthcare.clinic.identity.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacist_id", nullable = false)
    private User pharmacist;

    @Column(name = "dispensed_at", updatable = false)
    @Builder.Default
    private ZonedDateTime dispensedAt = ZonedDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String notes;
}
