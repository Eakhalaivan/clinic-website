package com.healthcare.clinic.ambulance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "ambulance_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmbulanceAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private EmergencyRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ambulance_id", nullable = false)
    private Ambulance ambulance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private AmbulanceDriver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paramedic_id")
    private AmbulanceParamedic paramedic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_destination_id")
    private HospitalDestination destination;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PROPOSED"; // PROPOSED, ASSIGNED, EN_ROUTE, AT_SCENE, TRANSPORTING, COMPLETED, CANCELLED

    @CreationTimestamp
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private ZonedDateTime assignedAt;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;
}
