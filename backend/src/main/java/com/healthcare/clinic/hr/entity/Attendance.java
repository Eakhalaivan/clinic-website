package com.healthcare.clinic.hr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "attendance", uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "check_in")
    private ZonedDateTime checkIn;

    @Column(name = "check_out")
    private ZonedDateTime checkOut;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "PRESENT"; // PRESENT, ABSENT, HALF_DAY, ON_LEAVE, LATE, OVERTIME

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id")
    private ShiftTemplate shift;

    @Column(name = "regularization_reason", length = 200)
    private String regularizationReason;

    @Column(name = "regularization_status", length = 30)
    private String regularizationStatus; // PENDING, APPROVED, REJECTED
    
    @Column(name = "approved_by")
    private Long approvedBy;
}
