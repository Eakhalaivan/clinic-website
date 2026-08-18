package com.healthcare.clinic.patient.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity(name="PatientAiChatMessage")
@Table(name = "ai_chat_messages")
@Data
public class AiChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(nullable = false)
    private String sender; // "USER" or "AI"

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    private Boolean containsSafetyFlag = false;

    @CreationTimestamp
    @Column(name = "sent_at", updatable = false)
    private ZonedDateTime sentAt;
}
