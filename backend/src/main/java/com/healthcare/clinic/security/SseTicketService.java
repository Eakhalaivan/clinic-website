package com.healthcare.clinic.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SseTicketService {

    public record TicketDetails(Long userId, boolean isAdminOrReceptionist) {}

    private final Map<String, TicketDetails> tickets = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public String generateTicket(Long userId, boolean isAdminOrReceptionist) {
        String ticketId = UUID.randomUUID().toString();
        tickets.put(ticketId, new TicketDetails(userId, isAdminOrReceptionist));
        
        // Ticket expires in 30 seconds
        scheduler.schedule(() -> tickets.remove(ticketId), 30, TimeUnit.SECONDS);
        
        return ticketId;
    }

    public TicketDetails consumeTicket(String ticketId) {
        if (ticketId == null) return null;
        return tickets.remove(ticketId);
    }
}
