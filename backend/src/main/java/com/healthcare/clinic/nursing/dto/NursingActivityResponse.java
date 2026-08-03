package com.healthcare.clinic.nursing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NursingActivityResponse {
    private String type; // "VITALS", "NOTE", "ASSIGNED"
    private String title;
    private String sub;
    private ZonedDateTime time;
    private String icon;
    private String color;
    private String bg;
}
