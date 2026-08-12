package com.healthcare.clinic.reception.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceptionPatientService {

    private final PatientProfileRepository patientProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Map<String, Object>> searchPatients(String query, String opNumber) {
        if (opNumber != null && !opNumber.trim().isEmpty()) {
            return patientProfileRepository.findByOpNumber(opNumber)
                    .map(this::mapToSearchResult)
                    .map(List::of)
                    .orElse(List.of());
        }

        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        
        String lowerQuery = query.toLowerCase();
        
        // Find users that match query (name or phone)
        // Note: For large datasets, a custom JPQL query in UserRepository would be better.
        // For simplicity, fetching all patients and filtering since it's a test clinic.
        List<PatientProfile> allProfiles = patientProfileRepository.findAll();
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (PatientProfile p : allProfiles) {
            User u = p.getUserId() != null ? userRepository.findById(p.getUserId()).orElse(null) : null;
            if (u != null) {
                String fName = u.getFirstName() != null ? u.getFirstName() : "";
                String lName = u.getLastName() != null ? u.getLastName() : "";
                String fullName = (fName + " " + lName).toLowerCase();
                String phone = u.getPhoneNumber() != null ? u.getPhoneNumber() : "";
                
                if (fullName.contains(lowerQuery) || phone.contains(lowerQuery)) {
                    results.add(mapToSearchResult(p, u));
                }
            }
        }
        return results;
    }
    
    private Map<String, Object> mapToSearchResult(PatientProfile p) {
        User u = p.getUserId() != null ? userRepository.findById(p.getUserId()).orElse(null) : null;
        return mapToSearchResult(p, u);
    }

    private Map<String, Object> mapToSearchResult(PatientProfile p, User u) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", p.getId());
        map.put("patientId", p.getUserId());
        map.put("opNumber", p.getOpNumber());
        map.put("gender", p.getGender());
        map.put("dateOfBirth", p.getDateOfBirth());
        map.put("branchId", p.getBranchId());
        
        if (u != null) {
            map.put("firstName", u.getFirstName());
            map.put("lastName", u.getLastName());
            map.put("phone", u.getPhoneNumber());
            map.put("email", u.getEmail());
        }
        return map;
    }
}
