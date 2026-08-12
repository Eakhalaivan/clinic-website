package com.healthcare.clinic.hr.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "leave_policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeavePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leave_type", nullable = false, length = 50)
    private String leaveType; // SICK, CASUAL, EARNED, MATERNITY

    @Column(name = "annual_allocation", nullable = false)
    private BigDecimal annualAllocation;

    @Column(name = "carry_forward_limit")
    @Builder.Default
    private BigDecimal carryForwardLimit = BigDecimal.ZERO;

    @Column(name = "is_encashable", nullable = false)
    @Builder.Default
    private Boolean isEncashable = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
