package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "patient_loyalty")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientLoyalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false, unique = true)
    private Long patientId;

    @Column(name = "points_balance", nullable = false)
    @Builder.Default
    private Integer pointsBalance = 0;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String tier = "BRONZE"; // BRONZE, SILVER, GOLD, PLATINUM

    // Phase 16 extended fields
    @Column(name = "lifetime_earned", nullable = false)
    @Builder.Default
    private Integer lifetimeEarned = 0;

    @Column(name = "lifetime_redeemed", nullable = false)
    @Builder.Default
    private Integer lifetimeRedeemed = 0;

    @Column(name = "last_earned_at")
    private ZonedDateTime lastEarnedAt;

    @Column(name = "last_redeemed_at")
    private ZonedDateTime lastRedeemedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
