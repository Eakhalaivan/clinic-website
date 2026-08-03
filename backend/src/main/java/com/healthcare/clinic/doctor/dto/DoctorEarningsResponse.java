package com.healthcare.clinic.doctor.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DoctorEarningsResponse {
    private BigDecimal today;
    private BigDecimal thisWeek;
    private BigDecimal thisMonth;
    private int totalConsultations;
    private List<Payout> recentPayouts;

    @Data
    @Builder
    public static class Payout {
        private String date;
        private String patient;
        private String type;
        private BigDecimal fee;
        private BigDecimal doctorShare;
    }
}
