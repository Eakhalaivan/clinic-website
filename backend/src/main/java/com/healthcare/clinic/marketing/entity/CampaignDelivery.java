package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "campaign_deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(nullable = false, length = 30)
    private String channel; // EMAIL, SMS, WHATSAPP, IN_APP

    /**
     * Status: QUEUED, SENT, DELIVERED, OPENED, CLICKED, BOUNCED, UNSUBSCRIBED, FAILED
     */
    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "QUEUED";

    @Column(name = "provider_message_id", length = 200)
    private String providerMessageId;

    @Column(name = "delivered_at")
    private ZonedDateTime deliveredAt;

    @Column(name = "opened_at")
    private ZonedDateTime openedAt;

    @Column(name = "clicked_at")
    private ZonedDateTime clickedAt;

    @Column(name = "bounced_at")
    private ZonedDateTime bouncedAt;

    @Column(name = "unsubscribed_at")
    private ZonedDateTime unsubscribedAt;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    /**
     * Unique key = campaignId + recipientId + channel to prevent duplicate sends.
     */
    @Column(name = "idempotency_key", length = 100, unique = true)
    private String idempotencyKey;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
