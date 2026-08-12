package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "membership_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "monthly_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyFee;

    @Column(name = "yearly_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal yearlyFee;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
