package com.healthcare.clinic.superadmin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actionType; // SETTINGS_CHANGE, RESTORE_BACKUP
    private Long requestedBy;
    private Long approvedBy;
    private String status; // PENDING, APPROVED, REJECTED
    
    @Column(columnDefinition = "TEXT")
    private String payloadDetails;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
