package com.healthcare.clinic.nursing.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.nursing.dto.NurseEscalationRequest;
import com.healthcare.clinic.nursing.dto.NursingChecklistRequest;
import com.healthcare.clinic.nursing.entity.NurseEscalation;
import com.healthcare.clinic.nursing.entity.NursingChecklist;
import com.healthcare.clinic.nursing.repository.NurseEscalationRepository;
import com.healthcare.clinic.nursing.repository.NursingChecklistRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NursingEscalationService {

    private final NurseEscalationRepository escalationRepository;
    private final NursingChecklistRepository checklistRepository;
    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;

    @Transactional
    public NurseEscalation createEscalation(NurseEscalationRequest request) {
        Long nurseId = SecurityUtils.getCurrentUserId();
        User nurse = userRepository.findById(nurseId).orElseThrow();
        PatientProfile patient = patientProfileRepository.findById(request.getPatientId()).orElseThrow();

        User doctor = null;
        if (request.getDoctorId() != null) {
            doctor = userRepository.findById(request.getDoctorId()).orElse(null);
        }

        NurseEscalation escalation = NurseEscalation.builder()
                .patient(patient)
                .encounterId(request.getEncounterId())
                .nurse(nurse)
                .doctor(doctor)
                .reason(request.getReason())
                .clinicalContext(request.getClinicalContext())
                .priority(request.getPriority())
                .build();
        return escalationRepository.save(escalation);
    }

    @Transactional
    public NurseEscalation resolveEscalation(Long escalationId, String resolutionNotes) {
        NurseEscalation escalation = escalationRepository.findById(escalationId).orElseThrow();
        escalation.setStatus("RESOLVED");
        escalation.setResolvedAt(ZonedDateTime.now());
        escalation.setResolutionNotes(resolutionNotes);
        return escalationRepository.save(escalation);
    }

    @Transactional(readOnly = true)
    public List<NurseEscalation> getPatientEscalations(Long patientId) {
        return escalationRepository.findByPatientIdOrderByEscalatedAtDesc(patientId);
    }

    @Transactional
    public NursingChecklist createChecklist(NursingChecklistRequest request) {
        Long nurseId = SecurityUtils.getCurrentUserId();
        User nurse = userRepository.findById(nurseId).orElseThrow();
        PatientProfile patient = patientProfileRepository.findById(request.getPatientId()).orElseThrow();

        NursingChecklist checklist = NursingChecklist.builder()
                .patient(patient)
                .encounterId(request.getEncounterId())
                .nurse(nurse)
                .checklistType(request.getChecklistType())
                .itemsJson(request.getItemsJson())
                .build();
        return checklistRepository.save(checklist);
    }

    @Transactional
    public NursingChecklist updateChecklistStatus(Long checklistId, String status, String itemsJson) {
        NursingChecklist checklist = checklistRepository.findById(checklistId).orElseThrow();
        checklist.setStatus(status);
        if (itemsJson != null) {
            checklist.setItemsJson(itemsJson);
        }
        if ("COMPLETED".equalsIgnoreCase(status)) {
            checklist.setCompletedAt(ZonedDateTime.now());
        }
        return checklistRepository.save(checklist);
    }

    @Transactional(readOnly = true)
    public List<NursingChecklist> getPatientChecklists(Long patientId) {
        return checklistRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }
}
