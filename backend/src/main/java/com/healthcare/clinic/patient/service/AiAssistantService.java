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
        return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
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

        // Generate Mock AI Response
        String aiResponseText = generateMockAiResponse(content);

        // Save AI Message
        AiChatMessage aiMessage = new AiChatMessage();
        aiMessage.setSessionId(session.getId());
        aiMessage.setSender("AI");
        aiMessage.setContent(aiResponseText);
        return messageRepository.save(aiMessage);
    }

    private String generateMockAiResponse(String userQuery) {
        String lowerQuery = userQuery.toLowerCase();
        if (lowerQuery.contains("fever") || lowerQuery.contains("headache")) {
            return "I am a virtual assistant, not a doctor. However, for a fever or headache, you might want to rest and stay hydrated. If symptoms persist for more than 3 days or get severe, please book a Home Visit or Teleconsultation through our portal.";
        } else if (lowerQuery.contains("appointment") || lowerQuery.contains("book")) {
            return "You can easily book an appointment by clicking the 'Book Clinic' or 'Video Call' buttons on your dashboard. Do you need help finding an available doctor?";
        } else if (lowerQuery.contains("lab") || lowerQuery.contains("report")) {
            return "You can view all your recent lab reports in the 'Lab Results' section of your dashboard. They are updated as soon as the laboratory verifies them.";
        } else {
            return "I understand you have a health query. I'm an AI assistant in training. Please use the clinic's Teleconsultation or Booking features for professional medical advice.";
        }
    }
}
