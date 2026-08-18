package com.healthcare.clinic.clinicaldecision.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DrugInteractionService {

    /**
     * Stubs a drug interaction check. Returns a list of severe interaction alerts.
     */
    public List<String> checkInteractions(List<String> medicationNames) {
        List<String> alerts = new ArrayList<>();
        
        // Simple stub logic: if prescribing Warfarin and Aspirin together
        boolean hasWarfarin = medicationNames.stream().anyMatch(n -> n.toLowerCase().contains("warfarin"));
        boolean hasAspirin = medicationNames.stream().anyMatch(n -> n.toLowerCase().contains("aspirin") || n.toLowerCase().contains("nsaid"));
        
        if (hasWarfarin && hasAspirin) {
            alerts.add("CRITICAL INTERACTION: Warfarin and Aspirin combination increases bleeding risk.");
        }
        
        // Another stub
        boolean hasSildenafil = medicationNames.stream().anyMatch(n -> n.toLowerCase().contains("sildenafil"));
        boolean hasNitrate = medicationNames.stream().anyMatch(n -> n.toLowerCase().contains("nitroglycerin"));
        if (hasSildenafil && hasNitrate) {
            alerts.add("CRITICAL INTERACTION: Sildenafil and Nitroglycerin combination can cause severe hypotension.");
        }

        return alerts;
    }
}
