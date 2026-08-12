package com.healthcare.clinic.radiology.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "imaging_procedures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagingProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 30)
    private String modality; // XRAY, MRI, CT, ULTRASOUND, PET

    @Column(name = "body_part", length = 100)
    private String bodyPart;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "requires_contrast", nullable = false)
    @Builder.Default
    private Boolean requiresContrast = false;

    @Column(name = "preparation_instructions", columnDefinition = "TEXT")
    private String preparationInstructions;

    @Column(name = "duration_minutes", nullable = false)
    @Builder.Default
    private Integer durationMinutes = 30;

    @Column(name = "turnaround_target_hours")
    private Integer turnaroundTargetHours;

    @Column(name = "radiation_safety_notes", columnDefinition = "TEXT")
    private String radiationSafetyNotes;
}
