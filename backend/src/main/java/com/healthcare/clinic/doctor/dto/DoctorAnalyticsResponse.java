package com.healthcare.clinic.doctor.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DoctorAnalyticsResponse {
    private double patientSatisfactionRating;
    private int reviewCount;
    private int avgConsultTimeMin;
    private int followUpRatePercent;
    private List<MonthlyVolume> monthlyVolume;

    @Data
    @Builder
    public static class MonthlyVolume {
        private String month;
        private int count;
    }
}
