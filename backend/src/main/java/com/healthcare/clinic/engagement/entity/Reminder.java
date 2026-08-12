package com.healthcare.clinic.engagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReminderType reminderType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private ZonedDateTime dueAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReminderStatus status = ReminderStatus.PENDING;

    private String sourceEntityType;
    private Long sourceEntityId;

    private String channelsSent; // Comma separated, e.g. "EMAIL,IN_APP"

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    public enum ReminderType {
        MEDICATION, FOLLOW_UP, PREVENTIVE_CARE, VACCINATION, CHRONIC_CARE, GENERAL_HEALTH, WELLNESS_PROGRAM
    }

    public enum ReminderStatus {
        PENDING, SENT, DISMISSED, COMPLETED
    }
}
