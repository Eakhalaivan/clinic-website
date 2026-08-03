package com.healthcare.clinic.identity.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.identity.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/auth/mfa")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class MfaController {

    private final UserRepository userRepository;
    private final OtpService otpService;

    @PostMapping("/enable")
    public ResponseEntity<?> enableMfa() {
        User user = getAuthenticatedUser();
        if (user == null) return ResponseEntity.status(401).build();
        
        user.setMfaEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok("MFA Enabled");
    }

    @PostMapping("/disable")
    public ResponseEntity<?> disableMfa() {
        User user = getAuthenticatedUser();
        if (user == null) return ResponseEntity.status(401).build();
        
        user.setMfaEnabled(false);
        userRepository.save(user);
        return ResponseEntity.ok("MFA Disabled");
    }
    
    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(email).orElse(null);
        }
        return null;
    }
}
