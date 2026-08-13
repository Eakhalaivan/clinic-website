package com.healthcare.clinic.emergency.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "emergency_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EmergencyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emergency_encounter_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private EmergencyEncounter emergencyEncounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordered_by_user_id", nullable = false)
    private User orderedBy;

    @Column(name = "order_type", nullable = false, length = 50)
    private String orderType; // LAB, RADIOLOGY, MEDICATION, PROCEDURE

    @Column(name = "reference_id", nullable = false)
    private Long referenceId; // ID of the LabTestRequest, ImagingRequest, or Prescription

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
