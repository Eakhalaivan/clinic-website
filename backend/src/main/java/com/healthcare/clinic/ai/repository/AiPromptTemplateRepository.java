package com.healthcare.clinic.ai.repository;
import com.healthcare.clinic.ai.entity.AiPromptTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface AiPromptTemplateRepository extends JpaRepository<AiPromptTemplate, Long> {
    Optional<AiPromptTemplate> findByTemplateKey(String templateKey);
}
