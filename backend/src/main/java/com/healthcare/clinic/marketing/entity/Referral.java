package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "referrals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "referrer_id", nullable = false)
    private Long referrerId;

    @Column(name = "referee_email", nullable = false)
    private String refereeEmail;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "PENDING"; // CREATED, SHARED, LEAD_CAPTURED, QUALIFIED, CONVERTED, REWARD_PENDING, REWARD_ISSUED, REJECTED, EXPIRED

    @Column(name = "reward_coupon", length = 50)
    private String rewardCoupon;

    // Phase 16 extended fields
    @Column(name = "program_id")
    private Long programId;

    @Column(name = "referral_code", length = 30, unique = true)
    private String referralCode;

    @Column(name = "referral_link", length = 500)
    private String referralLink;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(name = "converted_patient_id")
    private Long convertedPatientId;

    @Column(name = "qualifying_reference_id")
    private Long qualifyingReferenceId;

    /**
     * Fraud review: NOT_REQUIRED, PENDING_REVIEW, APPROVED, REJECTED
     */
    @Column(name = "fraud_review_status", nullable = false, length = 30)
    @Builder.Default
    private String fraudReviewStatus = "NOT_REQUIRED";

    @Column(name = "reward_issued_at")
    private ZonedDateTime rewardIssuedAt;

    @Column(name = "reward_type", length = 30)
    private String rewardType;

    @Column(name = "reward_value", precision = 10, scale = 2)
    private BigDecimal rewardValue;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
