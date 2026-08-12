package com.healthcare.clinic.ambulance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.math.BigDecimal;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "ambulance_trip_billings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmbulanceTripBilling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private TripHistory trip;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "dispatch_fee", precision = 10, scale = 2)
    private BigDecimal dispatchFee;

    @Column(name = "distance_fee", precision = 10, scale = 2)
    private BigDecimal distanceFee;

    @Column(name = "equipment_fee", precision = 10, scale = 2)
    private BigDecimal equipmentFee;

    @Column(name = "oxygen_fee", precision = 10, scale = 2)
    private BigDecimal oxygenFee;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "status", length = 50)
    @Builder.Default
    private String status = "PENDING"; // PENDING, INVOICED, PAID, WAIVED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
