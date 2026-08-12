package com.healthcare.clinic.hr.entity;

import com.healthcare.clinic.branch.entity.Branch;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "shift_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "break_duration_minutes", nullable = false)
    @Builder.Default
    private Integer breakDurationMinutes = 0;

    @Column(name = "is_overnight", nullable = false)
    @Builder.Default
    private Boolean isOvernight = false;

    @Column(name = "grace_period_minutes", nullable = false)
    @Builder.Default
    private Integer gracePeriodMinutes = 15;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
