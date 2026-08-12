package com.healthcare.clinic.doctor.medicine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "medicine_order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private MedicineOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_medicine_id", nullable = false)
    private DoctorMedicine doctorMedicine;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceAtOrder;
}
