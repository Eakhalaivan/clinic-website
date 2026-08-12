package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "campaigns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String campaignType = "GENERAL"; // HEALTH_AWARENESS, SERVICE_PROMOTION, APPOINTMENT_REMINDER, PREVENTIVE_SCREENING, RETENTION, REFERRAL, LOYALTY, MEMBERSHIP

    @Column(length = 100)
    private String objective;

    @Column(name = "target_segment_id")
    private Long targetSegmentId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false)
    @Builder.Default
    private List<String> channels = List.of("EMAIL");

    @Column(name = "content_template_id")
    private Long contentTemplateId;

    @Column(columnDefinition = "TEXT")
    private String content; // legacy field kept for backward compat

    @Column(precision = 12, scale = 2)
    private BigDecimal budget;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "frequency_cap_per_user", nullable = false)
    @Builder.Default
    private Integer frequencyCapPerUser = 1;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, REVIEW, APPROVED, SCHEDULED, ACTIVE, PAUSED, COMPLETED, ARCHIVED

    @Column(name = "sent_count", nullable = false)
    @Builder.Default
    private Integer sentCount = 0;

    @Column(name = "target_audience", nullable = false, length = 100)
    @Builder.Default
    private String targetAudience = "ALL_PATIENTS"; // kept for backward compat

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private ZonedDateTime approvedAt;

    @Column(name = "scheduled_at")
    private ZonedDateTime scheduledAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "success_metrics")
    private Map<String, Object> successMetrics;

    @Column(name = "archived_at")
    private ZonedDateTime archivedAt;

    @Column(name = "sent_at")
    private ZonedDateTime sentAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
