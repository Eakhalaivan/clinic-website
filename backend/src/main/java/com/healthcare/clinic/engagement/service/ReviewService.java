package com.healthcare.clinic.engagement.service;

import com.healthcare.clinic.engagement.entity.Review;
import com.healthcare.clinic.engagement.repository.ReviewRepository;
import com.healthcare.clinic.analytics.entity.DoctorPerformance;
import com.healthcare.clinic.analytics.repository.DoctorPerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DoctorPerformanceRepository doctorPerformanceRepository;

    @Transactional
    public Review submitReview(Review review) {
        review.setStatus(Review.ReviewStatus.PENDING_MODERATION);
        return reviewRepository.save(review);
    }

    @Transactional
    public Review moderateReview(Long reviewId, Review.ReviewStatus newStatus, Long moderatedByUserId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setStatus(newStatus);
        review.setModeratedByUserId(moderatedByUserId);
        
        Review savedReview = reviewRepository.save(review);

        if (newStatus == Review.ReviewStatus.PUBLISHED && review.getTargetType() == Review.TargetType.DOCTOR) {
            recalculateDoctorRating(review.getTargetId());
        }

        return savedReview;
    }

    private void recalculateDoctorRating(Long doctorId) {
        List<Review> publishedReviews = reviewRepository.findByTargetTypeAndTargetIdAndStatus(
                Review.TargetType.DOCTOR, doctorId, Review.ReviewStatus.PUBLISHED);

        if (publishedReviews.isEmpty()) {
            return;
        }

        double average = publishedReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        BigDecimal roundedAverage = BigDecimal.valueOf(average).setScale(1, RoundingMode.HALF_UP);

        // Update today's performance record (or create if missing)
        LocalDate today = LocalDate.now();
        Optional<DoctorPerformance> performanceOpt = doctorPerformanceRepository.findByDoctorUserIdAndDate(doctorId, today);
        
        DoctorPerformance performance;
        if (performanceOpt.isPresent()) {
            performance = performanceOpt.get();
        } else {
            performance = DoctorPerformance.builder()
                    .doctorUserId(doctorId)
                    .date(today)
                    .build();
        }

        performance.setRatingAverage(roundedAverage);
        doctorPerformanceRepository.save(performance);
    }
}
