package com.healthcare.clinic.telemedicine.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Filter;
import java.time.LocalDateTime;

@Entity
@Table(name = "teleconsult_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TeleconsultSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(nullable = false)
    private Long appointmentId;

    private String providerType; // TWILIO, DAILY, MOCK
    private String roomId;
    
    @Column(columnDefinition = "TEXT")
    private String patientToken;
    
    @Column(columnDefinition = "TEXT")
    private String doctorToken;

    private String status; // WAITING, IN_PROGRESS, COMPLETED, DISCONNECTED
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private String recordingUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
