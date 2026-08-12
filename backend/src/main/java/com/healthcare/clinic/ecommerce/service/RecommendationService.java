package com.healthcare.clinic.ecommerce.service;

import com.healthcare.clinic.ecommerce.entity.EcProductRecommendation;
import com.healthcare.clinic.ecommerce.entity.EcommerceProduct;
import com.healthcare.clinic.ecommerce.repository.EcProductRecommendationRepository;
import com.healthcare.clinic.ecommerce.repository.EcommerceProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final EcProductRecommendationRepository recommendationRepository;
    private final EcommerceProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<EcommerceProduct> getRecommendations(Long productId, String relationType) {
        List<Long> relatedIds = recommendationRepository.findAll().stream()
                .filter(r -> Boolean.TRUE.equals(r.getIsActive())
                        && productId.equals(r.getProductId())
                        && (relationType == null || relationType.equals(r.getRelationType())))
                .sorted((r1, r2) -> r2.getScore().compareTo(r1.getScore()))
                .map(EcProductRecommendation::getRelatedProductId)
                .toList();

        return productRepository.findAllById(relatedIds);
    }

    @Transactional
    public void addRecommendation(Long productId, Long relatedProductId, String relationType, java.math.BigDecimal score) {
        recommendationRepository.save(EcProductRecommendation.builder()
                .productId(productId)
                .relatedProductId(relatedProductId)
                .relationType(relationType)
                .score(score)
                .build());
    }
}
