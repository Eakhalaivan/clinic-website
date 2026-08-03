package com.healthcare.clinic.support.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.support.entity.SupportMessage;
import com.healthcare.clinic.support.entity.SupportTicket;
import com.healthcare.clinic.support.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    @GetMapping("/tickets")
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(supportService.getAllTickets());
    }

    @GetMapping("/my-tickets")
    public ResponseEntity<List<SupportTicket>> getMyTickets(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(supportService.getUserTickets(user.getId()));
    }

    @PostMapping("/tickets")
    public ResponseEntity<SupportTicket> createTicket(
            @RequestBody SupportTicket ticket,
            @RequestParam(required = false) String initialMessage,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(supportService.createTicket(ticket, user, initialMessage));
    }

    @GetMapping("/tickets/{ticketId}/messages")
    public ResponseEntity<List<SupportMessage>> getMessages(@PathVariable Long ticketId) {
        return ResponseEntity.ok(supportService.getTicketMessages(ticketId));
    }

    @PostMapping("/tickets/{ticketId}/messages")
    public ResponseEntity<SupportMessage> addMessage(
            @PathVariable Long ticketId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal User sender) {
        String text = body.get("message");
        boolean isAgent = sender.getRoles() != null && sender.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_CUSTOMER_SUPPORT") || r.getName().equals("ROLE_SUPER_ADMIN"));
        return ResponseEntity.ok(supportService.addMessage(ticketId, sender, text, isAgent));
    }

    @PatchMapping("/tickets/{ticketId}/status")
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<SupportTicket> updateStatus(@PathVariable Long ticketId, @RequestParam String status) {
        return ResponseEntity.ok(supportService.updateTicketStatus(ticketId, status));
    }
}
