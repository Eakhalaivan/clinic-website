package com.healthcare.clinic.pharmacy.entity;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.healthcare.clinic.pharmacy.entity.Medicine;

@Entity
@Table(name = "prescription_dispensed_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDispensedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispensed_id", nullable = false)
    private PrescriptionDispensed dispensed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private MedicineBatch batch;

    @Column(name = "quantity_dispensed", nullable = false)
    private Integer quantityDispensed;

    @Column(name = "price_charged", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceCharged;
}
