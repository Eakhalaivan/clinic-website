package com.healthcare.clinic.reception.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "queue_transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_id", nullable = false)
    private Long tokenId;

    @Column(name = "from_doctor_id")
    private Long fromDoctorId;

    @Column(name = "to_doctor_id")
    private Long toDoctorId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "transferred_at", nullable = false, updatable = false)
    @Builder.Default
    private ZonedDateTime transferredAt = ZonedDateTime.now();

    @Column(name = "transferred_by_user_id")
    private Long transferredByUserId;
}
