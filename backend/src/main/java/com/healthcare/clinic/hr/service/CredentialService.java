package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.hr.entity.EmployeeCredential;
import com.healthcare.clinic.hr.repository.EmployeeCredentialRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CredentialService {

    private final EmployeeCredentialRepository credentialRepository;

    public CredentialService(EmployeeCredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public EmployeeCredential addCredential(EmployeeCredential credential) {
        return credentialRepository.save(credential);
    }

    public List<EmployeeCredential> getEmployeeCredentials(Long employeeId) {
        return credentialRepository.findByEmployeeId(employeeId);
    }

    public EmployeeCredential verifyCredential(Long credentialId) {
        EmployeeCredential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credential ID"));
        
        credential.setIsVerified(true);
        return credentialRepository.save(credential);
    }

    @Scheduled(cron = "0 0 1 * * ?") // Run daily at 1 AM
    public void checkExpiringCredentials() {
        LocalDate alertThreshold = LocalDate.now().plusDays(30);
        List<EmployeeCredential> expiringCredentials = credentialRepository.findByExpiryDateBeforeAndStatus(alertThreshold, "ACTIVE");

        for (EmployeeCredential credential : expiringCredentials) {
            credential.setStatus("RENEWAL_PENDING");
            credentialRepository.save(credential);
            // In a real system, send email/notification here
        }
    }
}
