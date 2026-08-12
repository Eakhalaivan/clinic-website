package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "leads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Source: WEBSITE, KIOSK, WALK_IN, REFERRAL, CAMPAIGN, EVENT, PHONE, PARTNER, MANUAL
     */
    @Column(nullable = false, length = 50)
    private String source;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(length = 30)
    private String phone;

    @Column(length = 255)
    private String email;

    @Column(length = 200)
    private String interest;

    /**
     * Status: NEW, CONTACTED, QUALIFIED, APPOINTMENT_BOOKED, CONVERTED, NURTURING, LOST, ARCHIVED
     */
    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "NEW";

    @Column(nullable = false)
    @Builder.Default
    private Integer score = 0;

    /**
     * SHA-256 of normalized(phone + email) for deduplication.
     */
    @Column(name = "deduplication_key", length = 64, unique = true)
    private String deduplicationKey;

    @Column(name = "campaign_id")
    private Long campaignId;

    @Column(name = "referral_source", length = 200)
    private String referralSource;

    @Column(name = "communication_preference", length = 30)
    @Builder.Default
    private String communicationPreference = "ANY";

    @Column(name = "converted_patient_id")
    private Long convertedPatientId;

    @Column(name = "lost_reason")
    private String lostReason;

    @Column(name = "next_action_at")
    private ZonedDateTime nextActionAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
