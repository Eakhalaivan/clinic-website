package com.healthcare.clinic.nursing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity(name="NursingBed")
@Table(name = "beds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ward_id", nullable = false)
    private Long wardId;

    @Column(name = "room_id")
    @Builder.Default
    private Long roomId = 1L;

    @Column(name = "bed_number", nullable = false, length = 20)
    private String bedNumber;

    @Builder.Default
    @Column(length = 50)
    private String status = "AVAILABLE"; // AVAILABLE, OCCUPIED, CLEANING, MAINTENANCE, BLOCKED

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Version
    @Builder.Default
    private Long version = 0L;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}
