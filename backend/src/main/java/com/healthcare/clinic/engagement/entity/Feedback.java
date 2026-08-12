package com.healthcare.clinic.engagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeedbackCategory category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    private Long appointmentId;

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    public enum FeedbackCategory {
        FACILITY, STAFF, WAIT_TIME, BILLING, GENERAL
    }
}
