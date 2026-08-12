package com.healthcare.clinic.ambulance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ambulances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ambulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_number", nullable = false, unique = true, length = 50)
    private String vehicleNumber;

    @Column(length = 100)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private AmbulanceDriver driver;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "ambulance_type", length = 50)
    private String ambulanceType; // BLS, ALS, ICU

    @Column(name = "equipment", columnDefinition = "TEXT")
    private String equipment;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "fleet_registration_number", length = 50)
    private String fleetRegistrationNumber;

    @Column(name = "maintenance_status", length = 50)
    @Builder.Default
    private String maintenanceStatus = "OK";

    @Column(name = "current_latitude", precision = 10, scale = 8)
    private BigDecimal currentLatitude;

    @Column(name = "current_longitude", precision = 11, scale = 8)
    private BigDecimal currentLongitude;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "AVAILABLE"; // AVAILABLE, DISPATCHED, MAINTENANCE

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
