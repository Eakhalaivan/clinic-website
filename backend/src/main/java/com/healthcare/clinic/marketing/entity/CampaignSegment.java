package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.ZonedDateTime;
import java.util.Map;

@Entity
@Table(name = "campaign_segments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "criteria_json", nullable = false)
    @Builder.Default
    private Map<String, Object> criteriaJson = Map.of();

    @Column(name = "estimated_count", nullable = false)
    @Builder.Default
    private Integer estimatedCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
