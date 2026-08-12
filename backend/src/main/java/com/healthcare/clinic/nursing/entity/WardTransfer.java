package com.healthcare.clinic.nursing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "ward_transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WardTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "encounter_id", nullable = false)
    private Long encounterId;

    @Column(name = "source_bed_id")
    private Long sourceBedId;

    @Column(name = "destination_bed_id")
    private Long destinationBedId;

    @Column(name = "requested_by", nullable = false)
    private Long requestedBy;

    @Column(name = "requested_at")
    private ZonedDateTime requestedAt;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @Builder.Default
    @Column(length = 50)
    private String status = "REQUESTED"; // REQUESTED, APPROVED, IN_TRANSIT, COMPLETED, CANCELLED

    @Builder.Default
    @Column(length = 20)
    private String priority = "ROUTINE";

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "transfer_notes", columnDefinition = "TEXT")
    private String transferNotes;

    @PrePersist
    protected void onCreate() {
        if (requestedAt == null) requestedAt = ZonedDateTime.now();
    }
}
