package com.healthcare.clinic.reception.entity;

import com.healthcare.clinic.appointment.entity.Appointment;
import com.healthcare.clinic.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "queue_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "walk_in_id")
    private WalkInRegistration walkIn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "token_number", nullable = false)
    private Integer tokenNumber;

    @Column(name = "generated_at", updatable = false)
    @Builder.Default
    private ZonedDateTime generatedAt = ZonedDateTime.now();

    @Column(length = 50)
    @Builder.Default
    private String status = "WAITING"; // WAITING, CALLED, SERVED, SKIPPED
}
