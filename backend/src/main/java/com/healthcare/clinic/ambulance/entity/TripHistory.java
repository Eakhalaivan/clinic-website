package com.healthcare.clinic.ambulance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.math.BigDecimal;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "ambulance_trip_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private AmbulanceAssignment assignment;

    @Column(name = "total_distance_km", precision = 10, scale = 2)
    private BigDecimal totalDistanceKm;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "fuel_used", precision = 10, scale = 2)
    private BigDecimal fuelUsed;

    @Column(name = "outcome", length = 100)
    private String outcome;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
