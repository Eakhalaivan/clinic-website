package com.healthcare.clinic.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "controlled_substance_register")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControlledSubstanceRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pharmacy_prescription_id", nullable = false)
    private Long pharmacyPrescriptionId;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    @Column(name = "dispensed_quantity", nullable = false)
    private Integer dispensedQuantity;

    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @Column(name = "doctor_registration_number", nullable = false)
    private String doctorRegistrationNumber;

    @CreationTimestamp
    @Column(name = "dispensed_at", nullable = false, updatable = false)
    private LocalDateTime dispensedAt;

    @Column(name = "dispensed_by", nullable = false)
    private String dispensedBy;
}
