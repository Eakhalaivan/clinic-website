package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "nps_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NpsResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "survey_id", nullable = false, unique = true)
    private Long surveyId;

    /** NPS score 0–10 */
    @Column(name = "nps_score")
    private Integer npsScore;

    /** Star rating 1–5 */
    @Column
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(length = 100)
    private String category;

    /**
     * Escalation status: NONE, ESCALATED, RESOLVED
     */
    @Column(name = "escalation_status", nullable = false, length = 30)
    @Builder.Default
    private String escalationStatus = "NONE";

    @Column(name = "escalated_to")
    private Long escalatedTo;

    @Column(name = "resolved_at")
    private ZonedDateTime resolvedAt;

    @Column(name = "resolution_notes")
    private String resolutionNotes;

    @Column(name = "submitted_at", nullable = false)
    @Builder.Default
    private ZonedDateTime submittedAt = ZonedDateTime.now();
}
