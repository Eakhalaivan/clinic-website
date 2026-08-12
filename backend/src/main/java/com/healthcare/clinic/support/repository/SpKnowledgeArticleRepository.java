package com.healthcare.clinic.support.repository;

import com.healthcare.clinic.support.entity.SpKnowledgeArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpKnowledgeArticleRepository extends JpaRepository<SpKnowledgeArticle, Long> {
}
