package com.healthcare.clinic.support.service;

import com.healthcare.clinic.support.entity.SpComplaint;
import com.healthcare.clinic.support.entity.SpTicket;
import com.healthcare.clinic.support.repository.SpComplaintRepository;
import com.healthcare.clinic.support.repository.SpTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    
    private final SpTicketRepository ticketRepository;
    private final SpComplaintRepository complaintRepository;
    
    @Transactional
    public SpComplaint registerComplaint(Long ticketId, Long patientId, String severity) {
        SpTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        SpComplaint complaint = new SpComplaint();
        complaint.setTicket(ticket);
        complaint.setPatientId(patientId);
        complaint.setSeverity(severity);
        complaint.setStatus("RECEIVED");
        
        return complaintRepository.save(complaint);
    }
}
