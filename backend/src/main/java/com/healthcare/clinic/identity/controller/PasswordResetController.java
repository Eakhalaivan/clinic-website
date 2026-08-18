package com.healthcare.clinic.identity.controller;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.identity.service.OtpService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder encoder;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null) {
            otpService.generateAndSendOtp(user);
        }
        // Always return OK so we don't leak whether email exists
        return ResponseEntity.ok("If that email is in our system, we have sent a reset code.");
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid request");
        }
        
        boolean isValid = otpService.verifyOtp(request.getOtp(), user);
        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
        
        user.setPasswordHash(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        return ResponseEntity.ok("Password successfully reset.");
    }
}

@Data
class ForgotPasswordRequest {
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Email
    private String email;
}

@Data
class ResetPasswordRequest {
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Email
    private String email;
    private String otp;
    @jakarta.validation.constraints.NotBlank
    @jakarta.validation.constraints.Size(min = 6)
    private String newPassword;
}
