package com.healthcare.clinic.analytics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "doctor_performance", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"doctor_id", "date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DoctorPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorUserId;

    @Column(nullable = false)
    private LocalDate date;

    @Builder.Default
    private Integer appointmentsCompleted = 0;

    @Builder.Default
    private Integer appointmentsCancelled = 0;

    @Builder.Default
    private BigDecimal revenueGenerated = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal ratingAverage = BigDecimal.ZERO;

    @CreatedDate
    @Column(updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    private ZonedDateTime updatedAt;
}
