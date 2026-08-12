package com.healthcare.clinic.reception.service;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.reception.entity.QueueToken;
import com.healthcare.clinic.reception.entity.WalkInRegistration;
import com.healthcare.clinic.reception.repository.QueueTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class QueueTokenService {

    private final QueueTokenRepository queueTokenRepository;
    private final com.healthcare.clinic.branch.repository.BranchRepository branchRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public QueueToken generateToken(Branch branch, WalkInRegistration walkIn, Long appointmentId) {
        // Lock the branch to prevent concurrent token generation issues
        Branch lockedBranch = branchRepository.findByIdForUpdate(branch.getId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
                
        ZonedDateTime startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        int currentMaxToken = queueTokenRepository.findMaxTokenForBranchToday(lockedBranch.getId(), startOfDay).orElse(0);

        QueueToken token = QueueToken.builder()
                .branch(branch)
                .tokenNumber(currentMaxToken + 1)
                .status("WAITING")
                .generatedAt(ZonedDateTime.now())
                .generatedDate(LocalDate.now())
                .priorityLevel(0)
                .currentDepartment("GENERAL")
                .build();

        if (walkIn != null) {
            token.setWalkIn(walkIn);
        }
        
        if (appointmentId != null) {
            com.healthcare.clinic.appointment.entity.Appointment appointment = new com.healthcare.clinic.appointment.entity.Appointment();
            appointment.setId(appointmentId);
            token.setAppointment(appointment);
        }

        return queueTokenRepository.saveAndFlush(token);
    }
}
