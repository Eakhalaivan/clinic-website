package com.healthcare.clinic.engagement.repository;

import com.healthcare.clinic.engagement.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTargetTypeAndTargetIdAndStatus(Review.TargetType targetType, Long targetId, Review.ReviewStatus status);
    List<Review> findByStatus(Review.ReviewStatus status);
}
