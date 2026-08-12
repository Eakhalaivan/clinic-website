package com.healthcare.clinic.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ec_tax_rules")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EcTaxRule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_class", nullable = false, length = 50)
    private String taxClass;

    @Column(nullable = false, length = 100)
    @Builder.Default
    private String state = "ALL";

    @Column(name = "rate_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal ratePercent;

    @Column(name = "cgst_percent", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal cgstPercent = BigDecimal.ZERO;

    @Column(name = "sgst_percent", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal sgstPercent = BigDecimal.ZERO;

    @Column(name = "igst_percent", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal igstPercent = BigDecimal.ZERO;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
