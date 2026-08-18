package com.healthcare.clinic.patient.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.entity.AiChatMessage;
import com.healthcare.clinic.patient.entity.AiChatSession;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.AiChatMessageRepository;
import com.healthcare.clinic.patient.repository.AiChatSessionRepository;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiAssistantService {

    private final AiChatSessionRepository sessionRepository;
    private final AiChatMessageRepository messageRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final com.healthcare.clinic.ai.service.AIAssistantService globalAiService;

    private PatientProfile getPatientProfile(User user) {
        return patientProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient profile not found for user"));
    }

    @Transactional
    public AiChatSession getOrCreateActiveSession(User user) {
        PatientProfile profile = getPatientProfile(user);
        return sessionRepository.findFirstByPatientIdAndStatusOrderByCreatedAtDesc(profile.getId(), "Active")
                .orElseGet(() -> {
                    AiChatSession newSession = new AiChatSession();
                    newSession.setPatientId(profile.getId());
                    newSession.setStatus("Active");
                    return sessionRepository.save(newSession);
                });
    }

    public List<AiChatMessage> getSessionMessages(User user, Long sessionId) {
        // Simple authorization check could be added here
        return messageRepository.findBySessionIdOrderBySentAtAsc(sessionId);
    }

    @Transactional
    public AiChatMessage sendMessage(User user, Long sessionId, String content) {
        AiChatSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Save User Message
        AiChatMessage userMessage = new AiChatMessage();
        userMessage.setSessionId(session.getId());
        userMessage.setSender("USER");
        userMessage.setContent(content);
        messageRepository.save(userMessage);

        // Generate AI Response using global AI service
        String aiResponseText = globalAiService.generateChatResponse(content);

        // Save AI Message
        AiChatMessage aiMessage = new AiChatMessage();
        aiMessage.setSessionId(session.getId());
        aiMessage.setSender("AI");
        aiMessage.setContent(aiResponseText);
        return messageRepository.save(aiMessage);
    }
}


