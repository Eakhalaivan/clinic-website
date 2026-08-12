package com.healthcare.clinic.homevisit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Filter;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "home_visit_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class HomeVisitRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(nullable = false)
    private Long patientId;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private PatientAddress address;

    private String serviceType; // DOCTOR, NURSE, PHLEBOTOMIST
    private String symptoms;
    
    private LocalDateTime preferredDate;
    private String status; // REQUESTED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED

    private BigDecimal travelFee;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
