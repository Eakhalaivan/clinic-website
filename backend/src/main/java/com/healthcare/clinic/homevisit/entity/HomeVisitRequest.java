package com.healthcare.clinic.homevisit.entity;

import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "home_visit_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class HomeVisitRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @Column(name = "request_date", nullable = false)
    private ZonedDateTime requestDate;

    @Column(nullable = false, length = 20)
    private String status; // PENDING, APPROVED, COMPLETED, CANCELLED

    @Column(columnDefinition = "TEXT", nullable = false)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
