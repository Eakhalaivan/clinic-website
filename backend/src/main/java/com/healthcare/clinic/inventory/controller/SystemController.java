package com.healthcare.clinic.inventory.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController("pharmacySystemController")
@RequestMapping("/api/pharmacy/system")
public class SystemController {

    @GetMapping("/current-datetime")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPER_ADMIN','ROLE_PHARMACIST','ROLE_DOCTOR','ROLE_NURSE','ROLE_RECEPTION')")
    public Map<String, Object> getCurrentDatetime() {
        LocalDateTime now = LocalDateTime.now();

        Map<String, Object> response = new HashMap<>();
        response.put("current_date", now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        response.put("current_time", now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        response.put("current_datetime", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.000'Z'")));
        response.put("day_of_week", now.getDayOfWeek().name());

        int hour = now.getHour();
        String greeting = "Good Evening";
        if (hour < 12) {
            greeting = "Good Morning";
        } else if (hour < 17) {
            greeting = "Good Afternoon";
        }
        response.put("greeting", greeting);
        response.put("branch_name", "Main Branch");

        return response;
    }
}
