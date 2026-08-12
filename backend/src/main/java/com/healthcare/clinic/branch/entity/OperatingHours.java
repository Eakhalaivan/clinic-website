package com.healthcare.clinic.branch.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "operating_hours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    @JsonIgnore
    private Branch branch;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(nullable = false)
    private Integer dayOfWeek; // 1 = Monday, 7 = Sunday

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "is_closed")
    @Builder.Default
    private Boolean isClosed = false;
}
