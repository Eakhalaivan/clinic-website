package com.healthcare.clinic.ai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AIAssistantService {

    public String generateChatResponse(String input) {
        log.info("Received query for AI Assistant: {}", input);
        
        // This is a stub for the LLM integration (e.g. OpenAI or Gemini)
        String lowerInput = input.toLowerCase();
        
        if (lowerInput.contains("fever") || lowerInput.contains("headache")) {
            return "Fever and headache can stem from viral infections or fatigue. Make sure to stay hydrated. If fever exceeds 101°F for > 2 days, please consult Dr. Ramesh Rao.";
        } else if (lowerInput.contains("timing") || lowerInput.contains("hour")) {
            return "Aurelian Health Clinic is open Monday to Saturday from 08:00 AM to 08:00 PM. Emergency services operate 24/7.";
        }
        
        return "Thank you for your message. For acute medical symptoms, please book an appointment with our specialist doctor or contact emergency services.";
    }
}
