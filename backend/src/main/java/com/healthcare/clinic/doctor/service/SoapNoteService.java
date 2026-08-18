package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.doctor.entity.ClinicalEncounter;
import com.healthcare.clinic.doctor.entity.SoapNote;
import com.healthcare.clinic.doctor.repository.SoapNoteRepository;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SoapNoteService {

    private final SoapNoteRepository soapNoteRepository;
    private final ClinicalEncounterService encounterService;

    public Optional<SoapNote> getSoapNote(User user, Long encounterId) {
        // Validation of ownership happens inside getEncounter
        encounterService.getEncounter(user, encounterId);
        return soapNoteRepository.findByEncounterId(encounterId);
    }

    @Transactional
    public SoapNote saveSoapNote(User user, Long encounterId, SoapNote note) {
        ClinicalEncounter encounter = encounterService.getEncounter(user, encounterId);
        if (encounter.getStatus().equals("Finalized") || encounter.getStatus().equals("Signed") || encounter.getStatus().equals("Completed") || encounter.getStatus().equals("CLOSED")) {
            throw new RuntimeException("Cannot edit SOAP note for a finalized encounter");
        }

        Optional<SoapNote> existingOpt = soapNoteRepository.findByEncounterId(encounterId);
        if (existingOpt.isPresent()) {
            SoapNote existing = existingOpt.get();
            existing.setSubjective(note.getSubjective());
            existing.setObjective(note.getObjective());
            existing.setAssessment(note.getAssessment());
            existing.setPlan(note.getPlan());
            existing.setVersion(existing.getVersion() + 1);
            return soapNoteRepository.save(existing);
        } else {
            note.setEncounterId(encounterId);
            return soapNoteRepository.save(note);
        }
    }
}
