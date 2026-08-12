package com.healthcare.clinic.support.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.support.entity.SpMessage;
import com.healthcare.clinic.support.entity.SpTicket;
import com.healthcare.clinic.support.repository.SpMessageRepository;
import com.healthcare.clinic.support.repository.SpTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunicationService {

    private final SpTicketRepository ticketRepository;
    private final SpMessageRepository messageRepository;

    @Transactional
    public SpMessage addMessage(Long ticketId, User sender, String senderName, String content, boolean isInternalNote, String channel) {
        SpTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        SpMessage message = new SpMessage();
        message.setTicket(ticket);
        message.setSender(sender);
        message.setSenderName(senderName);
        message.setContent(content);
        message.setIsInternalNote(isInternalNote);
        message.setChannel(channel != null ? channel : "PORTAL");

        return messageRepository.save(message);
    }
}
