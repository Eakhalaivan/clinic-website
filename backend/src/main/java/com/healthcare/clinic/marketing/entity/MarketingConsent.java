package com.healthcare.clinic.marketing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "marketing_consents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketingConsent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "lead_id")
    private Long leadId;

    /**
     * Channel: EMAIL, SMS, WHATSAPP, PUSH, IN_APP
     */
    @Column(nullable = false, length = 30)
    private String channel;

    /**
     * State: OPTED_IN, OPTED_OUT
     */
    @Column(name = "consent_state", nullable = false, length = 20)
    @Builder.Default
    private String consentState = "OPTED_IN";

    @Column(name = "consent_source", length = 100)
    private String consentSource; // REGISTRATION, PORTAL, KIOSK, CAMPAIGN, MANUAL

    @Column(name = "wording_version", length = 20)
    private String wordingVersion;

    @Column(length = 100)
    private String purpose;

    @Column(name = "captured_at", nullable = false)
    @Builder.Default
    private ZonedDateTime capturedAt = ZonedDateTime.now();

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @Column(name = "withdrawn_at")
    private ZonedDateTime withdrawnAt;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "operator_id")
    private Long operatorId;
}
