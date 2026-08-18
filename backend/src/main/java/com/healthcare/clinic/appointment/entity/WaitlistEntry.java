package com.healthcare.clinic.appointment.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.patient.entity.PatientProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitlistEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientProfile patient;

    @Column(name = "desired_date_range_start")
    private LocalDateTime desiredDateRangeStart;

    @Column(name = "desired_date_range_end")
    private LocalDateTime desiredDateRangeEnd;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "status")
    @Builder.Default
    private String status = "WAITING"; // WAITING, PROMOTED, CANCELLED
}
