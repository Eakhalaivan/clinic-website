package com.healthcare.clinic.support.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.support.entity.SpCsatSurvey;
import com.healthcare.clinic.support.entity.SpTicket;
import com.healthcare.clinic.support.repository.SpCsatSurveyRepository;
import com.healthcare.clinic.support.repository.SpTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class CsatSurveyService {
    
    private final SpTicketRepository ticketRepository;
    private final SpCsatSurveyRepository surveyRepository;
    
    @Transactional
    public SpCsatSurvey sendSurvey(Long ticketId) {
        SpTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
                
        SpCsatSurvey survey = new SpCsatSurvey();
        survey.setTicket(ticket);
        survey.setPatient(ticket.getRequester());
        survey.setIsResponded(false);
        
        return surveyRepository.save(survey);
    }
    
    @Transactional
    public SpCsatSurvey submitResponse(Long surveyId, Integer rating, String feedback) {
        SpCsatSurvey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
                
        survey.setRating(rating);
        survey.setFeedback(feedback);
        survey.setIsResponded(true);
        survey.setRespondedAt(ZonedDateTime.now());
        
        return surveyRepository.save(survey);
    }
}
