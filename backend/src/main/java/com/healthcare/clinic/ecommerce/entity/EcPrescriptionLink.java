package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ec_prescription_links")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcPrescriptionLink {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_item_id", nullable = false)
    private Long orderItemId;

    @Column(name = "prescription_id", nullable = false)
    private Long prescriptionId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(name = "qty_authorised", nullable = false)
    private Integer qtyAuthorised;

    @Column(name = "qty_dispensed", nullable = false)
    @Builder.Default
    private Integer qtyDispensed = 0;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PENDING"; // PENDING, VERIFIED, REJECTED, DISPENSED

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "verified_at")
    private ZonedDateTime verifiedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();
}
