package com.healthcare.clinic.patient.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.ai.entity.AiChatMessage;
import com.healthcare.clinic.ai.entity.AiChatSession;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.ai.repository.AiChatMessageRepository;
import com.healthcare.clinic.ai.repository.AiChatSessionRepository;
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
        return sessionRepository.findByUserId(profile.getId())
                .stream().findFirst()
                .orElseGet(() -> {
                    AiChatSession newSession = AiChatSession.builder()
                            .userId(profile.getId())
                            .userRole("PATIENT")
                            .title("Health Assistant")
                            .build();
                    return sessionRepository.save(newSession);
                });
    }

    public List<AiChatMessage> getSessionMessages(User user, Long sessionId) {
        return messageRepository.findBySessionIdOrderBySentAtAsc(sessionId);
    }

    @Transactional
    public AiChatMessage sendMessage(User user, Long sessionId, String content) {
        AiChatSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Save User Message
        AiChatMessage userMessage = AiChatMessage.builder()
                .session(session)
                .sender("USER")
                .content(content)
                .build();
        messageRepository.save(userMessage);

        // Generate Mock AI Response
        String aiResponseText = generateMockAiResponse(content);

        // Save AI Message
        AiChatMessage aiMessage = AiChatMessage.builder()
                .session(session)
                .sender("AI")
                .content(aiResponseText)
                .build();
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
