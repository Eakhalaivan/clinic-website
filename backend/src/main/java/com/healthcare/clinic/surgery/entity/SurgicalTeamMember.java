package com.healthcare.clinic.surgery.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "surgical_team_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SurgicalTeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_booking_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private SurgeryBooking surgeryBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User teamMember;

    @Column(nullable = false, length = 50)
    private String role; // ANESTHESIOLOGIST, SCRUB_NURSE, CIRCULATING_NURSE, ASSISTANT_SURGEON

    @CreatedDate
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private ZonedDateTime assignedAt;
}
