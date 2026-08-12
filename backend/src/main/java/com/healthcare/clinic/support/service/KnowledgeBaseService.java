package com.healthcare.clinic.support.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.support.entity.SpKnowledgeArticle;
import com.healthcare.clinic.support.repository.SpKnowledgeArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KnowledgeBaseService {
    
    private final SpKnowledgeArticleRepository articleRepository;
    
    @Transactional
    public SpKnowledgeArticle createArticle(String title, String summary, String content, String audience, User author) {
        SpKnowledgeArticle article = new SpKnowledgeArticle();
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);
        article.setAudience(audience != null ? audience : "PUBLIC");
        article.setStatus("DRAFT");
        article.setAuthor(author);
        article.setVersion(1);
        
        return articleRepository.save(article);
    }
    
    public List<SpKnowledgeArticle> searchArticles(String query, String userAudience) {
        // Simplified search implementation
        return articleRepository.findAll().stream()
            .filter(a -> "PUBLISHED".equals(a.getStatus()))
            .filter(a -> "PUBLIC".equals(a.getAudience()) || a.getAudience().equals(userAudience))
            .filter(a -> a.getTitle().toLowerCase().contains(query.toLowerCase()) || 
                         (a.getSummary() != null && a.getSummary().toLowerCase().contains(query.toLowerCase())))
            .collect(Collectors.toList());
    }
}
