package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "campaign_automations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignAutomation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Trigger: NEW_LEAD, INACTIVE_PATIENT, MISSED_APPOINTMENT, BIRTHDAY,
     *          PREVENTIVE_CARE, MEMBERSHIP_RENEWAL, REFERRAL_COMPLETION,
     *          FEEDBACK_REQUEST, ABANDONED_BOOKING, POST_VISIT
     */
    @Column(name = "trigger_type", nullable = false, length = 50)
    private String triggerType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "eligibility_criteria", nullable = false)
    @Builder.Default
    private Map<String, Object> eligibilityCriteria = Map.of();

    @Column(name = "delay_minutes", nullable = false)
    @Builder.Default
    private Integer delayMinutes = 0;

    @Column(nullable = false, length = 30)
    private String channel; // EMAIL, SMS, WHATSAPP, IN_APP

    @Column(name = "template_id")
    private Long templateId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "stop_conditions", nullable = false)
    @Builder.Default
    private List<String> stopConditions = List.of();

    @Column(name = "frequency_cap", nullable = false)
    @Builder.Default
    private Integer frequencyCap = 1;

    @Column(name = "quiet_hours_start")
    private LocalTime quietHoursStart;

    @Column(name = "quiet_hours_end")
    private LocalTime quietHoursEnd;

    @Column(name = "branch_id")
    private Long branchId;

    /**
     * Status: DRAFT, APPROVED, ACTIVE, PAUSED, ARCHIVED
     */
    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "DRAFT";

    @Column(nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
