package com.healthcare.clinic.ambulance.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import com.healthcare.clinic.tenant.entity.Tenant;
import com.healthcare.clinic.branch.entity.Branch;
import lombok.*;
import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;

@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = Long.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Entity
@Table(name = "emergency_patient_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyPatientRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Branch branch;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private EmergencyRequest request;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "vitals_summary", columnDefinition = "TEXT")
    private String vitalsSummary;

    @Column(columnDefinition = "TEXT")
    private String interventions;

    @Column(name = "medication_administered", columnDefinition = "TEXT")
    private String medicationAdministered;

    @Column(name = "crew_notes", columnDefinition = "TEXT")
    private String crewNotes;

    @Column(name = "handover_summary", columnDefinition = "TEXT")
    private String handoverSummary;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
