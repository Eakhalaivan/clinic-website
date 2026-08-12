package com.healthcare.clinic.support.entity;

import com.healthcare.clinic.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "sp_sla_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpSlaPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String priority;

    private String category;

    @Column(name = "first_response_minutes", nullable = false)
    private Integer firstResponseMinutes;

    @Column(name = "resolution_minutes", nullable = false)
    private Integer resolutionMinutes;

    @Column(name = "business_hours_only")
    private Boolean businessHoursOnly;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "is_active")
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}
