package com.healthcare.clinic.ai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_chat_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class AiChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private AiChatSession session;

    private String sender; // USER, AI, SYSTEM

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean containsSafetyFlag;

    @CreationTimestamp
    private LocalDateTime sentAt;
}
