package com.healthcare.clinic.superadmin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Table(name = "active_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "tenant_id")
    private Long tenantId;

    private String tokenHash;
    private String ipAddress;
    private String userAgent;
    private String device;
    private String locationEstimate;

    private LocalDateTime loginTime;
    private LocalDateTime lastActivity;
    private boolean revoked;
}
