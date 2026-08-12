package com.healthcare.clinic.nursing.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.nursing.dto.FallRiskAssessmentRequest;
import com.healthcare.clinic.nursing.dto.NursingCarePlanRequest;
import com.healthcare.clinic.nursing.dto.NursingNoteRequest;
import com.healthcare.clinic.nursing.dto.PainAssessmentRequest;
import com.healthcare.clinic.nursing.entity.FallRiskAssessment;
import com.healthcare.clinic.nursing.entity.NursingCarePlan;
import com.healthcare.clinic.nursing.entity.NursingNote;
import com.healthcare.clinic.nursing.entity.PainAssessment;
import com.healthcare.clinic.nursing.repository.FallRiskAssessmentRepository;
import com.healthcare.clinic.nursing.repository.NursingCarePlanRepository;
import com.healthcare.clinic.nursing.repository.NursingNoteRepository;
import com.healthcare.clinic.nursing.repository.PainAssessmentRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NursingDocumentationService {

    private final NursingNoteRepository nursingNoteRepository;
    private final NursingCarePlanRepository nursingCarePlanRepository;
    private final FallRiskAssessmentRepository fallRiskAssessmentRepository;
    private final PainAssessmentRepository painAssessmentRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final UserRepository userRepository;

    @Transactional
    public NursingNote createNursingNote(NursingNoteRequest request) {
        Long nurseId = SecurityUtils.getCurrentUserId();
        User nurse = userRepository.findById(nurseId).orElseThrow();
        PatientProfile patient = patientProfileRepository.findById(request.getPatientId()).orElseThrow();

        NursingNote note = NursingNote.builder()
                .patient(patient)
                .encounterId(request.getEncounterId())
                .nurse(nurse)
                .noteType(request.getNoteType())
                .note(request.getContent())
                .build();
        return nursingNoteRepository.save(note);
    }

    @Transactional(readOnly = true)
    public List<NursingNote> getPatientNursingNotes(Long patientId) {
        return nursingNoteRepository.findByPatientIdOrderByRecordedAtDesc(patientId);
    }

    @Transactional
    public NursingCarePlan createCarePlan(NursingCarePlanRequest request) {
        Long nurseId = SecurityUtils.getCurrentUserId();
        User nurse = userRepository.findById(nurseId).orElseThrow();
        PatientProfile patient = patientProfileRepository.findById(request.getPatientId()).orElseThrow();

        NursingCarePlan plan = NursingCarePlan.builder()
                .patient(patient)
                .encounterId(request.getEncounterId())
                .nurse(nurse)
                .diagnosis(request.getDiagnosis())
                .goals(request.getGoals())
                .interventions(request.getInterventions())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .build();
        return nursingCarePlanRepository.save(plan);
    }

    @Transactional(readOnly = true)
    public List<NursingCarePlan> getPatientCarePlans(Long patientId) {
        return nursingCarePlanRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Transactional
    public FallRiskAssessment createFallRiskAssessment(FallRiskAssessmentRequest request) {
        Long nurseId = SecurityUtils.getCurrentUserId();
        User nurse = userRepository.findById(nurseId).orElseThrow();
        PatientProfile patient = patientProfileRepository.findById(request.getPatientId()).orElseThrow();

        String riskLevel = "LOW";
        if (request.getScore() >= 13) {
            riskLevel = "HIGH";
        } else if (request.getScore() >= 6) {
            riskLevel = "MODERATE";
        }

        FallRiskAssessment assessment = FallRiskAssessment.builder()
                .patient(patient)
                .encounterId(request.getEncounterId())
                .nurse(nurse)
                .score(request.getScore())
                .riskLevel(riskLevel)
                .notes(request.getNotes())
                .build();
        return fallRiskAssessmentRepository.save(assessment);
    }

    @Transactional(readOnly = true)
    public List<FallRiskAssessment> getPatientFallRiskAssessments(Long patientId) {
        return fallRiskAssessmentRepository.findByPatientIdOrderByAssessedAtDesc(patientId);
    }

    @Transactional
    public PainAssessment createPainAssessment(PainAssessmentRequest request) {
        Long nurseId = SecurityUtils.getCurrentUserId();
        User nurse = userRepository.findById(nurseId).orElseThrow();
        PatientProfile patient = patientProfileRepository.findById(request.getPatientId()).orElseThrow();

        PainAssessment assessment = PainAssessment.builder()
                .patient(patient)
                .encounterId(request.getEncounterId())
                .nurse(nurse)
                .painScore(request.getPainScore())
                .painLocation(request.getPainLocation())
                .painCharacteristics(request.getPainCharacteristics())
                .interventions(request.getInterventions())
                .build();
        return painAssessmentRepository.save(assessment);
    }

    @Transactional(readOnly = true)
    public List<PainAssessment> getPatientPainAssessments(Long patientId) {
        return painAssessmentRepository.findByPatientIdOrderByAssessedAtDesc(patientId);
    }
}
