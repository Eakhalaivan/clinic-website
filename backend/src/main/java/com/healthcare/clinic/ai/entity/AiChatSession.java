package com.healthcare.clinic.ai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Filter;
import java.time.LocalDateTime;

@Entity(name="AiAiChatSession")
@Table(name = "ai_chat_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class AiChatSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "user_id")
    private Long userId;

    private String userRole; // PATIENT, DOCTOR
    private String title;
    
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
