package com.healthcare.clinic.pharmacy.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Column(name = "batch_id", length = 36)
    private String batchId;

    @Column(name = "quantity_dispensed", nullable = false)
    private Integer quantityDispensed;

    @Column(name = "price_charged", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceCharged;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;
}
