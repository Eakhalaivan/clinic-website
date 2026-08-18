package com.healthcare.clinic.homevisit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Filter;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import com.healthcare.clinic.patient.entity.PatientProfile;

@Entity(name="HomevisitHomeVisitRequest")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private PatientAddress address;
    
    @Column(columnDefinition = "TEXT")
    private String addressText; // From naveen's string address

    @Column(name="service_type")
    private String serviceType; // DOCTOR, NURSE, PHLEBOTOMIST
    private String symptoms;
    
    @Column(columnDefinition = "TEXT")
    private String notes; // From naveen

    @Column(name="preferred_date")
    private LocalDateTime preferredDate;
    
    @Column(name = "request_date")
    private ZonedDateTime requestDate; // From naveen
    
    private String status; // REQUESTED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED

    private BigDecimal travelFee;
    
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
