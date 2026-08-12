package com.healthcare.clinic.support.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.support.entity.SpTicket;
import com.healthcare.clinic.support.repository.SpTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final SpTicketRepository ticketRepository;
    private final SlaService slaService;

    @Transactional
    public SpTicket createTicket(SpTicket ticketRequest, User requester) {
        if (ticketRequest.getIdempotencyKey() != null) {
            Optional<SpTicket> existing = ticketRepository.findByIdempotencyKey(ticketRequest.getIdempotencyKey());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        SpTicket ticket = new SpTicket();
        ticket.setTicketNumber("TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        ticket.setIdempotencyKey(ticketRequest.getIdempotencyKey());
        ticket.setRequester(requester);
        ticket.setGuestEmail(ticketRequest.getGuestEmail());
        ticket.setGuestPhone(ticketRequest.getGuestPhone());
        ticket.setSubject(ticketRequest.getSubject());
        ticket.setDescription(ticketRequest.getDescription());
        ticket.setChannel(ticketRequest.getChannel() != null ? ticketRequest.getChannel() : "PORTAL");
        ticket.setCategory(ticketRequest.getCategory() != null ? ticketRequest.getCategory() : "GENERAL");
        ticket.setSubcategory(ticketRequest.getSubcategory());
        ticket.setPriority(ticketRequest.getPriority() != null ? ticketRequest.getPriority() : "MEDIUM");
        ticket.setStatus("NEW");
        ticket.setBranch(ticketRequest.getBranch());
        
        ticket.setReferenceAppointmentId(ticketRequest.getReferenceAppointmentId());
        ticket.setReferenceOrderId(ticketRequest.getReferenceOrderId());
        ticket.setReferenceInvoiceId(ticketRequest.getReferenceInvoiceId());

        // Attach SLA deadlines
        slaService.applySlaPolicy(ticket);

        return ticketRepository.save(ticket);
    }

    public List<SpTicket> getTicketsByRequester(User requester) {
        return ticketRepository.findByRequesterIdOrderByCreatedAtDesc(requester.getId());
    }

    public List<SpTicket> getOpenTickets() {
        return ticketRepository.findByStatusIn(List.of("NEW", "OPEN", "IN_PROGRESS", "PENDING_CUSTOMER", "PENDING_INTERNAL", "ESCALATED"));
    }

    @Transactional
    public SpTicket updateTicketStatus(Long ticketId, String newStatus, User actor) {
        SpTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        ticket.setStatus(newStatus);
        
        if ("RESOLVED".equals(newStatus)) {
            ticket.setResolvedAt(java.time.ZonedDateTime.now());
        } else if ("CLOSED".equals(newStatus)) {
            ticket.setClosedAt(java.time.ZonedDateTime.now());
        }
        
        return ticketRepository.save(ticket);
    }
}
