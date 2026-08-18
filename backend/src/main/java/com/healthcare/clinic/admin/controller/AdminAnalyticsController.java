package com.healthcare.clinic.admin.controller;

import com.healthcare.clinic.admin.dto.AdminDashboardMetricsDto;
import com.healthcare.clinic.admin.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
public class AdminAnalyticsController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<AdminDashboardMetricsDto> getDashboardMetrics() {
        return ResponseEntity.ok(adminDashboardService.getDashboardMetrics());
    }

    @GetMapping("/stream")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public SseEmitter streamDashboardMetrics() {
        SseEmitter emitter = new SseEmitter(60000L);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                emitter.send(adminDashboardService.getDashboardMetrics());
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }, 0, 10, TimeUnit.SECONDS);
        return emitter;
    }
}
