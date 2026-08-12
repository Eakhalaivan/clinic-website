package com.healthcare.clinic.support.service;

import com.healthcare.clinic.support.entity.SpTicket;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class SlaService {
    
    public void applySlaPolicy(SpTicket ticket) {
        // In a real implementation, this would look up the appropriate policy
        // based on priority, category, branch, etc.
        // For now, we apply some basic rules
        int responseMins = 60;
        int resolutionMins = 1440; // 24 hours
        
        if ("CRITICAL".equals(ticket.getPriority())) {
            responseMins = 15;
            resolutionMins = 120;
        } else if ("HIGH".equals(ticket.getPriority())) {
            responseMins = 30;
            resolutionMins = 240;
        }
        
        ticket.setFirstResponseDueAt(ZonedDateTime.now().plusMinutes(responseMins));
        ticket.setResolutionDueAt(ZonedDateTime.now().plusMinutes(resolutionMins));
        ticket.setSlaStatus("ON_TRACK");
    }
}
