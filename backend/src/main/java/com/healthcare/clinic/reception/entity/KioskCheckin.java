package com.healthcare.clinic.reception.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "kiosk_checkins")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KioskCheckin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "checkin_method", nullable = false, length = 50)
    @Builder.Default
    private String checkinMethod = "KIOSK"; // KIOSK, RECEPTION, WALK_IN

    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private String status = "PENDING"; // PENDING, VERIFIED, CHECKED_IN, NO_SHOW

    @Column(name = "kiosk_station", length = 100)
    private String kioskStation;

    @Column(name = "verified_at")
    private ZonedDateTime verifiedAt;

    @Column(name = "verified_by_staff")
    private Long verifiedByStaff;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
