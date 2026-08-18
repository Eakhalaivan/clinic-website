package com.healthcare.clinic.pharmacy.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pharmacy_prescription_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyPrescriptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_prescription_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private PharmacyPrescriptionRecord pharmacyPrescription;

    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @Column(name = "type", length = 50)
    private String type;

    @Column(nullable = false, length = 100)
    private String dosage;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "duration")
    private String duration;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(length = 50)
    private String strength;

    @Column(length = 50)
    private String timing;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "medicine_id")
    private Long medicineId;

    @Column(name = "prescribed_quantity")
    @Builder.Default
    private Integer prescribedQuantity = 1;

    @Column(name = "dispensed_quantity")
    @Builder.Default
    private Integer dispensedQuantity = 0;

    @Column(name = "remaining_quantity")
    @Builder.Default
    private Integer remainingQuantity = 1;

    @Column(name = "substitution_allowed")
    @Builder.Default
    private Boolean substitutionAllowed = false;

    @Column(name = "dispensed_medicine_id")
    private Long dispensedMedicineId;

    @Column(name = "dispensed_medicine_name")
    private String dispensedMedicineName;
}
