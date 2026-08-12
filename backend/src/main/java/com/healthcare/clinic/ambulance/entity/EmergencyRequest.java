package com.healthcare.clinic.ambulance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "emergency_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_number", nullable = false, unique = true, length = 50)
    private String requestNumber;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "pickup_address", nullable = false, columnDefinition = "TEXT")
    private String pickupAddress;

    @Column(name = "pickup_latitude", precision = 10, scale = 8)
    private BigDecimal pickupLatitude;

    @Column(name = "pickup_longitude", precision = 11, scale = 8)
    private BigDecimal pickupLongitude;

    @Column(name = "caller_name", length = 100)
    private String callerName;

    @Column(name = "caller_phone", length = 30)
    private String callerPhone;

    @Column(name = "caller_relation", length = 50)
    private String callerRelation;

    @Column(name = "incident_description", columnDefinition = "TEXT")
    private String incidentDescription;

    @Column(name = "clinical_red_flags", columnDefinition = "TEXT")
    private String clinicalRedFlags;

    @Column(name = "emergency_type", nullable = false, length = 100)
    @Builder.Default
    private String emergencyType = "CARDIAC";

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String priority = "CRITICAL"; // CRITICAL, URGENT, ROUTINE

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "REQUESTED"; // REQUESTED, DISPATCHED, EN_ROUTE, COMPLETED, CANCELLED

    @Column(name = "hospital_destination_id")
    private Long hospitalDestinationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_ambulance_id")
    private Ambulance assignedAmbulance;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private ZonedDateTime requestedAt;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;
}
