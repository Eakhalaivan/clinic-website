package com.healthcare.clinic.doctor.controller;

import com.healthcare.clinic.doctor.entity.SoapNote;
import com.healthcare.clinic.doctor.service.SoapNoteService;
import com.healthcare.clinic.identity.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctor/encounters/{encounterId}/soap-note")
@RequiredArgsConstructor
public class SoapNoteController {

    private final SoapNoteService soapNoteService;

    @GetMapping
    public ResponseEntity<SoapNote> getSoapNote(@AuthenticationPrincipal User user, @PathVariable Long encounterId) {
        return soapNoteService.getSoapNote(user, encounterId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<SoapNote> saveSoapNote(
            @AuthenticationPrincipal User user,
            @PathVariable Long encounterId,
            @RequestBody SoapNote soapNote) {
        return ResponseEntity.ok(soapNoteService.saveSoapNote(user, encounterId, soapNote));
    }
}
