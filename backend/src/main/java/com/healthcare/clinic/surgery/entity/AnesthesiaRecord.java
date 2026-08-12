package com.healthcare.clinic.surgery.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "anesthesia_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AnesthesiaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_booking_id", nullable = false, unique = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private SurgeryBooking surgeryBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anesthesiologist_id", nullable = false)
    private DoctorProfile anesthesiologist;

    @Column(name = "anesthesia_type", nullable = false, length = 100)
    private String anesthesiaType; // GENERAL, REGIONAL, LOCAL, MAC

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
