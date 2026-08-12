package com.healthcare.clinic.ambulance.entity;

import jakarta.persistence.*;
import lombok.*;
import com.healthcare.clinic.identity.entity.User;

@Entity
@Table(name = "ambulance_drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmbulanceDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "license_number", nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(name = "branch_id")
    private Long branchId;
}
