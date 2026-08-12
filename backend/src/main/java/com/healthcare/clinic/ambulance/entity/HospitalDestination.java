package com.healthcare.clinic.ambulance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hospital_destinations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "emergency_capacity")
    private Integer emergencyCapacity;
    
    @Column(name = "is_internal_branch", nullable = false)
    @Builder.Default
    private Boolean isInternalBranch = false;
    
    @Column(name = "branch_id")
    private Long branchId;
}
