package com.healthcare.clinic.inpatient.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "bed_transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BedTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Admission admission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_bed_id", nullable = false)
    private Bed fromBed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_bed_id", nullable = false)
    private Bed toBed;

    @Column(name = "transferred_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime transferredAt = ZonedDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transferred_by_user_id", nullable = false)
    private User transferredBy;
}
