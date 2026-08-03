package com.healthcare.clinic.doctor.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "doctor_prescription_template_items")
public class PrescriptionTemplateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    @JsonIgnore
    private PrescriptionTemplate template;

    private String medicationName;
    private String type;
    private String strength;
    private String dosage;
    private String frequency;
    private String duration;
    private String timing;
    private String instructions;
}
