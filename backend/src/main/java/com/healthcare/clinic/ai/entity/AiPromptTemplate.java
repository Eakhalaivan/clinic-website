package com.healthcare.clinic.ai.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "ai_prompt_templates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class AiPromptTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(nullable = false, unique = true)
    private String templateKey;

    @Column(columnDefinition = "TEXT")
    private String systemPrompt;

    private String modelConfigId;
    private boolean isActive;
}
