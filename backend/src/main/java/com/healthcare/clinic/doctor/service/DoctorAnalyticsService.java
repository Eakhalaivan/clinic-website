package com.healthcare.clinic.doctor.service;

import com.healthcare.clinic.appointment.entity.Appointment;
import com.healthcare.clinic.appointment.repository.AppointmentRepository;
import com.healthcare.clinic.doctor.dto.DoctorAnalyticsResponse;
import com.healthcare.clinic.doctor.entity.DoctorFollowUp;
import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.doctor.repository.DoctorFollowUpRepository;
import com.healthcare.clinic.doctor.repository.DoctorProfileRepository;
import com.healthcare.clinic.engagement.entity.Review;
import com.healthcare.clinic.engagement.repository.ReviewRepository;
import com.healthcare.clinic.analytics.entity.DoctorPerformance;
import com.healthcare.clinic.analytics.repository.DoctorPerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DoctorAnalyticsService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorFollowUpRepository followUpRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final ReviewRepository reviewRepository;
    private final DoctorPerformanceRepository doctorPerformanceRepository;

    @Transactional(readOnly = true)
    public DoctorAnalyticsResponse getAnalyticsForDoctor(Long userId) {
        List<Appointment> allAppointments = appointmentRepository.findByDoctor_UserId(userId);
        
        DoctorProfile doctor = doctorProfileRepository.findByUserId(userId).orElse(null);
        Long doctorId = doctor != null ? doctor.getId() : -1L;
        
        List<Appointment> completedAppointments = allAppointments.stream()
                .filter(a -> "COMPLETED".equals(a.getStatus()))
                .toList();
        
        int totalCompleted = completedAppointments.size();
        
        // Avg consult time
        int totalMinutes = 0;
        for (Appointment a : completedAppointments) {
            if (a.getSlot() != null) {
                totalMinutes += (int) Duration.between(a.getSlot().getStartTime(), a.getSlot().getEndTime()).toMinutes();
            }
        }
        int avgConsultTimeMin = totalCompleted > 0 ? totalMinutes / totalCompleted : 15; // default 15
        
        // Follow-up rate
        List<DoctorFollowUp> followUps = followUpRepository.findByDoctorIdOrderByFollowUpDateAsc(doctorId);
        int followUpRatePercent = totalCompleted > 0 ? (int) Math.round((double) followUps.size() / totalCompleted * 100) : 0;
        if (followUpRatePercent > 100) followUpRatePercent = 100;
        
        // Monthly Volume (last 6 months)
        ZonedDateTime now = ZonedDateTime.now();
        Map<Month, Integer> monthCounts = new HashMap<>();
        for (int i = 5; i >= 0; i--) {
            monthCounts.put(now.minusMonths(i).getMonth(), 0);
        }
        
        for (Appointment a : completedAppointments) {
            if (a.getCreatedAt() != null) {
                Month m = a.getCreatedAt().getMonth();
                if (monthCounts.containsKey(m)) {
                    monthCounts.put(m, monthCounts.get(m) + 1);
                }
            }
        }
        
        List<DoctorAnalyticsResponse.MonthlyVolume> monthlyVolume = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            Month m = now.minusMonths(i).getMonth();
            String monthName = m.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            monthlyVolume.add(DoctorAnalyticsResponse.MonthlyVolume.builder()
                    .month(monthName)
                    .count(monthCounts.get(m))
                    .build());
        }

        // Get actual rating and review count
        List<Review> publishedReviews = reviewRepository.findByTargetTypeAndTargetIdAndStatus(
                Review.TargetType.DOCTOR, doctorId, Review.ReviewStatus.PUBLISHED);
        int reviewCount = publishedReviews.size();
        
        double patientSatisfactionRating = 0.0;
        if (reviewCount > 0) {
            patientSatisfactionRating = doctorPerformanceRepository.findByDoctorUserIdAndDate(userId, java.time.LocalDate.now())
                    .map(p -> p.getRatingAverage().doubleValue())
                    .orElseGet(() -> publishedReviews.stream().mapToInt(Review::getRating).average().orElse(0.0));
        }

        return DoctorAnalyticsResponse.builder()
                .patientSatisfactionRating(patientSatisfactionRating)
                .reviewCount(reviewCount)
                .avgConsultTimeMin(avgConsultTimeMin)
                .followUpRatePercent(followUpRatePercent)
                .monthlyVolume(monthlyVolume)
                .build();
    }
}
