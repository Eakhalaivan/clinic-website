package com.healthcare.clinic.superadmin.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Table(name = "backup_histories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackupHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String backupType; // FULL, INCREMENTAL
    private String storageLocation;
    private String status; // SUCCESS, FAILED
    private Long sizeBytes;
    
    @Column(name = "tenant_id")
    private Long tenantId;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
