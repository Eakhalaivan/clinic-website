package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "referral_programs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferralProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    /**
     * Reward type: POINTS, COUPON, GIFT_CARD
     */
    @Column(name = "reward_type", nullable = false, length = 30)
    private String rewardType;

    @Column(name = "reward_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal rewardValue;

    /**
     * Qualifying event: APPOINTMENT_COMPLETED, PAID_INVOICE
     */
    @Column(name = "qualifying_event", nullable = false, length = 50)
    private String qualifyingEvent;

    @Column(name = "max_reward_per_referrer", nullable = false)
    @Builder.Default
    private Integer maxRewardPerReferrer = 10;

    @Column(name = "max_reward_per_referee", nullable = false)
    @Builder.Default
    private Integer maxRewardPerReferee = 1;

    @Column(name = "expiry_days", nullable = false)
    @Builder.Default
    private Integer expiryDays = 90;

    @Column(name = "fraud_review_required", nullable = false)
    @Builder.Default
    private Boolean fraudReviewRequired = false;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "ACTIVE";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
