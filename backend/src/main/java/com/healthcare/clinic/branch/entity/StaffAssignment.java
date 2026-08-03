package com.healthcare.clinic.branch.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "staff_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class StaffAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;

    @Column(nullable = false)
    private Long userId; // References identity-service

    @Column(nullable = false, length = 50)
    private String role; // DOCTOR, RECEPTIONIST, ADMIN

    @Builder.Default
    private Boolean isPrimary = true;

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;
}
