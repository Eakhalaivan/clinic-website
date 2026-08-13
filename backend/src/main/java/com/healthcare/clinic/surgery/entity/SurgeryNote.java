package com.healthcare.clinic.surgery.entity;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Entity
@Table(name = "surgery_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SurgeryNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_booking_id", nullable = false, unique = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private SurgeryBooking surgeryBooking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgeon_id", nullable = false)
    private DoctorProfile surgeon;

    @Column(name = "pre_op_diagnosis", columnDefinition = "TEXT")
    private String preOpDiagnosis;

    @Column(name = "post_op_diagnosis", columnDefinition = "TEXT")
    private String postOpDiagnosis;

    @Column(name = "procedure_performed", columnDefinition = "TEXT")
    private String procedurePerformed;

    @Column(name = "findings", columnDefinition = "TEXT")
    private String findings;
    
    @Column(name = "complications", columnDefinition = "TEXT")
    private String complications;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
