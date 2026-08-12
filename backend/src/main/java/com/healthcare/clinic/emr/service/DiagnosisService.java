package com.healthcare.clinic.emr.service;

import com.healthcare.clinic.clinicaldecision.event.DiagnosisAddedEvent;
import com.healthcare.clinic.emr.entity.Diagnosis;
import com.healthcare.clinic.emr.repository.DiagnosisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Diagnosis addDiagnosis(Diagnosis diagnosis) {
        Diagnosis saved = diagnosisRepository.save(diagnosis);

        if ("CONFIRMED".equalsIgnoreCase(saved.getStatus())) {
            eventPublisher.publishEvent(DiagnosisAddedEvent.builder()
                    .patientId(saved.getPatientId())
                    .recordId(saved.getId())
                    .icd10Code(saved.getIcd10Code())
                    .diagnosisName(saved.getDiagnosisName())
                    .doctorId(saved.getDiagnosingDoctorId())
                    .build());
        }

        return saved;
    }

    public List<Diagnosis> getDiagnosesByPatient(Long patientId) {
        return diagnosisRepository.findByPatientId(patientId);
    }
}
