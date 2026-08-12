package com.healthcare.clinic.doctor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class VoiceToTextService {

    public String transcribe(MultipartFile audioFile) {
        log.info("Received audio file for transcription: {}, size: {}", audioFile.getOriginalFilename(), audioFile.getSize());
        
        // Mock transcription process
        return "Patient presents with a 3-day history of productive cough, fever, and chills. Denies shortness of breath. Past medical history significant for asthma.";
    }
}
