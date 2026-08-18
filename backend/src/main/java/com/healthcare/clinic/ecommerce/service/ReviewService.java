package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcReview;
import com.healthcare.clinic.ecommerce.entity.EcReviewResponse;
import com.healthcare.clinic.ecommerce.repository.EcReviewRepository;
import com.healthcare.clinic.ecommerce.repository.EcReviewResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ecommerceReviewService")
@RequiredArgsConstructor
public class ReviewService {

    private final EcReviewRepository reviewRepository;
    private final EcReviewResponseRepository reviewResponseRepository;

    @Transactional(readOnly = true)
    public List<EcReview> getProductReviews(Long productId) {
        return reviewRepository.findAll().stream()
                .filter(r -> productId.equals(r.getProductId()) && "APPROVED".equals(r.getModerationStatus()))
                .toList();
    }

    @Transactional
    public EcReview submitReview(Long productId, Long patientId, int rating, String title, String body, boolean isVerifiedPurchase) {
        return reviewRepository.save(EcReview.builder()
                .productId(productId)
                .patientId(patientId)
                .rating(rating)
                .title(title)
                .body(body)
                .moderationStatus("PENDING")
                .isVerifiedPurchase(isVerifiedPurchase)
                .build());
    }

    @Transactional
    public void moderateReview(Long reviewId, Long adminId, boolean approve, String note) {
        EcReview review = reviewRepository.findById(reviewId).orElseThrow();
        review.setModerationStatus(approve ? "APPROVED" : "REJECTED");
        review.setModeratedBy(adminId);
        review.setModerationNote(note);
        reviewRepository.save(review);
    }

    @Transactional
    public EcReviewResponse respondToReview(Long reviewId, Long responderId, String body) {
        EcReview review = reviewRepository.findById(reviewId).orElseThrow();
        return reviewResponseRepository.save(EcReviewResponse.builder()
                .review(review)
                .responderId(responderId)
                .body(body)
                .build());
    }
}
