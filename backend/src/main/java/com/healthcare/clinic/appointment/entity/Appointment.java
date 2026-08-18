package com.healthcare.clinic.appointment.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.patient.entity.PatientProfile;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import com.healthcare.clinic.tenant.entity.Tenant;
import com.healthcare.clinic.branch.entity.Branch;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = Long.class))
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Appointment {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private AppointmentSlot slot;

    @Column(name = "idempotency_key", unique = true, length = 100)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status; // BOOKED, CANCELLED, COMPLETED, NO_SHOW, etc.

    @Column(name = "appointment_type", length = 30)
    private String appointmentType; // IN_PERSON, TELECONSULT, HOME_VISIT

    @Column(columnDefinition = "TEXT")
    private String reasonForVisit;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "branch_id", insertable = false, updatable = false)
    private Long branchId;

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;

    @Version
    private Long version;
}
