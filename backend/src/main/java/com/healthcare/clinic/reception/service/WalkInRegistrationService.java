package com.healthcare.clinic.reception.service;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.reception.entity.QueueToken;
import com.healthcare.clinic.reception.entity.WalkInRegistration;
import com.healthcare.clinic.reception.repository.QueueTokenRepository;
import com.healthcare.clinic.reception.repository.WalkInRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalkInRegistrationService {

    private final WalkInRegistrationRepository walkInRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final BranchRepository branchRepository;
    private final QueueTokenRepository queueTokenRepository;

    @Transactional
    public WalkInRegistration registerWalkIn(Long branchId, Long patientId, String reasonForVisit, Integer priorityLevel, String department) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
        
        PatientProfile patient = null;
        if (patientId != null) {
            patient = patientProfileRepository.findById(patientId)
                    .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        }

        WalkInRegistration walkIn = WalkInRegistration.builder()
                .branch(branch)
                .patient(patient)
                .reasonForVisit(reasonForVisit)
                .status("WAITING")
                .build();
                
        walkIn = walkInRepository.save(walkIn);

        // Generate Queue Token
        ZonedDateTime startOfDay = ZonedDateTime.now().toLocalDate().atStartOfDay(ZonedDateTime.now().getZone());
        Integer maxToken = queueTokenRepository.findMaxTokenForBranchToday(branchId, startOfDay).orElse(0);

        QueueToken token = QueueToken.builder()
                .branch(branch)
                .walkIn(walkIn)
                .tokenNumber(maxToken + 1)
                .status("WAITING")
                .priorityLevel(priorityLevel != null ? priorityLevel : 0)
                .currentDepartment(department != null ? department : "GENERAL")
                .build();
                
        queueTokenRepository.save(token);

        return walkIn;
    }
}
