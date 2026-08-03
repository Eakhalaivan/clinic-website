package com.healthcare.clinic.clinicaldecision.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "care_pathway_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarePathwayStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pathway_id", nullable = false)
    @JsonIgnore
    private PatientCarePathway pathway;

    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", nullable = false, length = 30)
    private StepType stepType;

    @Column(name = "due_offset_days", nullable = false)
    @Builder.Default
    private Integer dueOffsetDays = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private StepStatus status = StepStatus.PENDING;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @Column(name = "completed_by")
    private Long completedBy;
}
