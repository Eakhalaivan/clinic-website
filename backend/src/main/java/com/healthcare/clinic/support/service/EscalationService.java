package com.healthcare.clinic.support.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.support.entity.SpEscalation;
import com.healthcare.clinic.support.entity.SpTicket;
import com.healthcare.clinic.support.repository.SpEscalationRepository;
import com.healthcare.clinic.support.repository.SpTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EscalationService {
    
    private final SpTicketRepository ticketRepository;
    private final SpEscalationRepository escalationRepository;
    
    @Transactional
    public SpEscalation escalateTicket(Long ticketId, User escalatedBy, String targetTeam, String reason) {
        SpTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        ticket.setStatus("ESCALATED");
        ticketRepository.save(ticket);
        
        SpEscalation escalation = new SpEscalation();
        escalation.setTicket(ticket);
        escalation.setEscalatedBy(escalatedBy);
        escalation.setTargetTeam(targetTeam);
        escalation.setReason(reason);
        escalation.setStatus("PENDING");
        
        return escalationRepository.save(escalation);
    }
}
