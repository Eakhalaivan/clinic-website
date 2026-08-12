package com.healthcare.clinic.hr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "salary_components")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_structure_id", nullable = false)
    private SalaryStructure salaryStructure;

    @Column(nullable = false, length = 50)
    private String name; // HRA, Transport Allowance, Provident Fund, Tax

    @Column(nullable = false, length = 30)
    private String type; // ALLOWANCE, DEDUCTION

    @Column(name = "amount_type", nullable = false, length = 30)
    private String amountType; // FIXED, PERCENTAGE

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal value;
}
