package com.healthcare.clinic.ai.service;

import com.healthcare.clinic.ai.entity.AiChatMessage;
import com.healthcare.clinic.ai.entity.AiChatSession;
import com.healthcare.clinic.ai.repository.AiChatMessageRepository;
import com.healthcare.clinic.ai.repository.AiChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiPatientService {
    
    private final AiChatSessionRepository sessionRepository;
    private final AiChatMessageRepository messageRepository;

    public AiChatSession startSession(Long userId, Long tenantId) {
        AiChatSession session = AiChatSession.builder()
            .userId(userId)
            .userRole("PATIENT")
            .tenantId(tenantId)
            .title("New Chat")
            .build();
        return sessionRepository.save(session);
    }

    public AiChatMessage processPatientMessage(Long sessionId, String content, Long userId, Long tenantId) {
        AiChatSession session = sessionRepository.findById(sessionId).orElseThrow();
        
        // Save User Message
        AiChatMessage userMessage = AiChatMessage.builder()
            .session(session)
            .tenantId(tenantId)
            .sender("USER")
            .content(content)
            .build();
        messageRepository.save(userMessage);
        
        // Simulate LLM Processing with safety guardrails
        String aiResponse = simulateLlmResponse(content);
        boolean flagged = aiResponse.contains("[URGENT]");
        
        AiChatMessage aiMessage = AiChatMessage.builder()
            .session(session)
            .tenantId(tenantId)
            .sender("AI")
            .content(aiResponse)
            .containsSafetyFlag(flagged)
            .build();
            
        return messageRepository.save(aiMessage);
    }
    
    private String simulateLlmResponse(String input) {
        input = input.toLowerCase();
        if(input.contains("chest pain") || input.contains("bleeding")) {
            return "[URGENT] If you are experiencing a medical emergency, please call emergency services immediately or visit the nearest emergency room. I am an AI and cannot provide emergency medical advice.";
        }
        return "I can help you schedule an appointment or summarize your recent lab results. I am an AI assistant and my responses should be reviewed by a clinician.";
    }

    public List<AiChatMessage> getHistory(Long sessionId) {
        return messageRepository.findBySessionIdOrderBySentAtAsc(sessionId);
    }
}
