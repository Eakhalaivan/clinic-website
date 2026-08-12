package com.healthcare.clinic.superadmin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import lombok.*;

@Entity
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Table(name = "retention_policies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetentionPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dataCategory; // PATIENT_RECORDS, AUDIT_LOGS

    private int retentionDays;
    private boolean legalHold;
    
    @Column(name = "tenant_id")
    private Long tenantId;
}
