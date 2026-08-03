package com.healthcare.clinic.nursing.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.nursing.repository.NursePatientAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("nursingSecurity")
@RequiredArgsConstructor
public class NursingSecurityService {

    private final NursePatientAssignmentRepository assignmentRepository;

    public boolean isAssigned(Authentication authentication, Long patientId) {
        if (authentication == null || !authentication.isAuthenticated()) return false;
        
        // If super admin, allow access
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            return true;
        }

        com.healthcare.clinic.security.UserPrincipal user = (com.healthcare.clinic.security.UserPrincipal) authentication.getPrincipal();
        return assignmentRepository.existsByNurseIdAndPatientIdAndStatus(user.getUserId(), patientId, "ACTIVE");
    }
}
