package com.healthcare.clinic.tenant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "legal_entity_name", length = 150)
    private String legalEntityName;

    @Column(name = "tax_registration_number", length = 50)
    private String taxRegistrationNumber;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // TRIAL, ACTIVE, SUSPENDED, ARCHIVED

    @Column(name = "subscription_plan", length = 50)
    @Builder.Default
    private String subscriptionPlan = "FREE";

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
