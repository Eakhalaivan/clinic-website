package com.healthcare.clinic.superadmin.service;

import com.healthcare.clinic.superadmin.entity.ActiveSession;
import com.healthcare.clinic.superadmin.repository.ActiveSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionManagementService {
    private final ActiveSessionRepository repository;

    public List<ActiveSession> getActiveSessions() {
        return repository.findAll(); // Should be paginated in reality
    }

    public void revokeSession(Long sessionId) {
        repository.findById(sessionId).ifPresent(s -> {
            s.setRevoked(true);
            repository.save(s);
        });
    }
}
