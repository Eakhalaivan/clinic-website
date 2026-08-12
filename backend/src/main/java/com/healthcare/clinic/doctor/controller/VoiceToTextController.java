package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.service.VoiceToTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/doctor/voice-to-text")
@RequiredArgsConstructor
public class VoiceToTextController {

    private final VoiceToTextService voiceToTextService;

    @PostMapping("/transcribe")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Map<String, String>> transcribeAudio(@RequestParam("file") MultipartFile file) {
        String transcription = voiceToTextService.transcribe(file);
        return ResponseEntity.ok(Map.of("transcription", transcription));
    }
}
