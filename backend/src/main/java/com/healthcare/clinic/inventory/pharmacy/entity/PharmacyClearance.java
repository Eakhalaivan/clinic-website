package com.healthcare.clinic.inventory.pharmacy.entity;

import com.healthcare.clinic.identity.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "pharmacy_clearances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyClearance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "patient_name", nullable = false, length = 100)
    private String patientName;

    @Column(name = "uhid", nullable = false, length = 50)
    private String uhid;

    @Column(name = "ward", length = 50)
    private String ward;

    @Column(name = "admission_date")
    private ZonedDateTime admissionDate;

    @Column(name = "total_due", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalDue;

    @Column(name = "advance_adjusted", nullable = false, precision = 12, scale = 2)
    private BigDecimal advanceAdjusted;

    @Column(name = "net_payable", nullable = false, precision = 12, scale = 2)
    private BigDecimal netPayable;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "cleared_at")
    private ZonedDateTime clearedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cleared_by")
    private User clearedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
