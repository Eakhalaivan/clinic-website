package com.healthcare.clinic.nursing.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "medication_administration_records")
@Data
public class MedicationAdministrationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long patientId;
    private Long prescriptionItemId;
    
    private String patientName;
    private String bedNumber;
    private String medicationName;
    private String dosage;
    
    private LocalDateTime scheduledTime;
    private LocalDateTime administeredAt;
    
    @Enumerated(EnumType.STRING)
    private MarStatus status;
    
    private Long administeredByUserId;
    private String notes;
}
