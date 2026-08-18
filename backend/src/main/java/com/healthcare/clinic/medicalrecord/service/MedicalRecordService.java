package com.healthcare.clinic.medicalrecord.service;

import com.healthcare.clinic.exception.ResourceNotFoundException;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.medicalrecord.dto.MedicalRecordRequest;
import com.healthcare.clinic.medicalrecord.dto.MedicalRecordResponse;
import com.healthcare.clinic.medicalrecord.entity.MedicalRecord;
import com.healthcare.clinic.medicalrecord.repository.MedicalRecordRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository recordRepository;
    private final UserRepository userRepository;
    private final com.healthcare.clinic.doctor.repository.ClinicalEncounterRepository encounterRepository;

    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getRecordsForPatient(Long patientId) {
        return encounterRepository.findByPatientIdOrderByCreatedAtDesc(patientId)
                .stream()
                .filter(e -> "CLOSED".equals(e.getStatus()) || "Completed".equals(e.getStatus()))
                .map(this::mapEncounterToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getRecordsByDoctor(Long doctorId) {
        return encounterRepository.findByDoctorIdOrderByCreatedAtDesc(doctorId)
                .stream()
                .filter(e -> "CLOSED".equals(e.getStatus()) || "Completed".equals(e.getStatus()))
                .map(this::mapEncounterToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicalRecordResponse createRecord(MedicalRecordRequest request) {
        Long doctorId = SecurityUtils.getCurrentUserId();
        
        // Verify patient exists
        if (!userRepository.existsById(request.getPatientId())) {
            throw new ResourceNotFoundException("Patient not found with id: " + request.getPatientId());
        }

        MedicalRecord record = MedicalRecord.builder()
                .patientId(request.getPatientId())
                .doctorId(doctorId)
                .recordType(request.getRecordType())
                .title(request.getTitle())
                .notes(request.getNotes())
                .build();

        MedicalRecord saved = recordRepository.save(record);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public MedicalRecordResponse getRecordById(Long id) {
        MedicalRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return mapToResponse(record);
    }

    @Transactional
    public void deleteRecord(Long id) {
        MedicalRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        
        Long currentUserId = SecurityUtils.getCurrentUserId();
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"));

        if (!isAdmin && (currentUserId == null || !record.getDoctorId().equals(currentUserId))) {
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to delete this medical record.");
        }
        
        recordRepository.delete(record);
    }

    private MedicalRecordResponse mapToResponse(MedicalRecord record) {
        String doctorName = userRepository.findById(record.getDoctorId())
                .map(u -> u.getFirstName() + " " + u.getLastName())
                .orElse("Unknown Doctor");

        return MedicalRecordResponse.builder()
                .id(record.getId())
                .patientId(record.getPatientId())
                .doctorId(record.getDoctorId())
                .doctorName(doctorName)
                .recordType(record.getRecordType())
                .title(record.getTitle())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }

    private MedicalRecordResponse mapEncounterToResponse(com.healthcare.clinic.doctor.entity.ClinicalEncounter encounter) {
        String doctorName = userRepository.findById(encounter.getDoctorId())
                .map(u -> u.getFirstName() + " " + u.getLastName())
                .orElse("Unknown Doctor");

        return MedicalRecordResponse.builder()
                .id(encounter.getId())
                .patientId(encounter.getPatientId())
                .doctorId(encounter.getDoctorId())
                .doctorName(doctorName)
                .recordType(com.healthcare.clinic.medicalrecord.entity.RecordType.CONSULTATION_NOTE)
                .title("Clinical Encounter")
                .notes(encounter.getChiefComplaint() != null ? "Complaint: " + encounter.getChiefComplaint() : "Completed Encounter")
                .createdAt(encounter.getCreatedAt() != null ? encounter.getCreatedAt().toLocalDateTime() : null)
                .updatedAt(encounter.getUpdatedAt() != null ? encounter.getUpdatedAt().toLocalDateTime() : null)
                .build();
    }
}
