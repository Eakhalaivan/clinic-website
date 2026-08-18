package com.healthcare.clinic.ai.service;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PhiSanitizer {

    private static final Pattern PHONE_PATTERN = Pattern.compile("\\b\\d{3}[-.]?\\d{3}[-.]?\\d{4}\\b");
    private static final Pattern SSN_PATTERN = Pattern.compile("\\b\\d{3}-\\d{2}-\\d{4}\\b");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b");

    public String sanitize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String sanitized = input;
        sanitized = PHONE_PATTERN.matcher(sanitized).replaceAll("[PHONE_REMOVED]");
        sanitized = SSN_PATTERN.matcher(sanitized).replaceAll("[SSN_REMOVED]");
        sanitized = EMAIL_PATTERN.matcher(sanitized).replaceAll("[EMAIL_REMOVED]");
        
        return sanitized;
    }
}
