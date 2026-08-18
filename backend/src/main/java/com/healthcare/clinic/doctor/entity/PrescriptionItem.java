package com.healthcare.clinic.doctor.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prescription_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    @ToString.Exclude
    private Prescription prescription;

    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @Column(name = "type", length = 50)
    private String type;

    @Column(nullable = false, length = 100)
    private String dosage;

    @Column(nullable = false, length = 100)
    private String frequency;

    @Column(nullable = false, length = 100)
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(length = 50)
    private String strength;

    @Column(length = 50)
    private String timing;

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
}
