package com.healthcare.clinic.emr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "procedure_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcedureRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "procedure_name", nullable = false, length = 255)
    private String procedureName;

    @Column(name = "procedure_code", length = 100)
    private String procedureCode; // CPT style

    @Column(name = "performed_date", nullable = false)
    private LocalDate performedDate;

    @Column(name = "performed_by_user_id", nullable = false)
    private Long performedByUserId;

    @Column(name = "encounter_id")
    private Long encounterId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "surgery_booking_id")
    private Long surgeryBookingId; // Major OT surgeries link here
}
