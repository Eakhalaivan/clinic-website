package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "lead_activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lead_id", nullable = false)
    private Long leadId;

    /**
     * Type: CALL, EMAIL, SMS, WHATSAPP, TASK, APPOINTMENT_BOOKED, NOTE, ESCALATION, REASSIGNMENT, LOST
     */
    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(length = 100)
    private String outcome;

    @Column(name = "next_step")
    private String nextStep;

    @Column(name = "due_at")
    private ZonedDateTime dueAt;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @Column(name = "performed_by")
    private Long performedBy;

    @Column(length = 30)
    private String channel;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
