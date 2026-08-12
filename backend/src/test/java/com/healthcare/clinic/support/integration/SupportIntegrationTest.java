package com.healthcare.clinic.support.integration;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.support.entity.SpTicket;
import com.healthcare.clinic.support.repository.SpTicketRepository;
import com.healthcare.clinic.support.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class SupportIntegrationTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpTicketRepository ticketRepository;

    @Test
    public void testTicketCreationAndSla() {
        // Find a test user or assume one exists for the test profile
        User patient = userRepository.findByEmail("patient1@example.com").orElse(null);
        
        if (patient != null) {
            SpTicket req = new SpTicket();
            req.setSubject("Need help with billing");
            req.setDescription("I cannot see my last invoice.");
            req.setCategory("BILLING");
            req.setPriority("HIGH");
            
            SpTicket ticket = ticketService.createTicket(req, patient);
            
            assertNotNull(ticket.getId());
            assertNotNull(ticket.getTicketNumber());
            assertEquals("HIGH", ticket.getPriority());
            assertNotNull(ticket.getFirstResponseDueAt());
            
            System.out.println("Ticket created successfully: " + ticket.getTicketNumber());
        }
    }
}
