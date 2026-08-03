package com.healthcare.clinic.clinicaldecision.service;

import com.healthcare.clinic.clinicaldecision.entity.CdsRule;
import com.healthcare.clinic.clinicaldecision.entity.Severity;
import com.healthcare.clinic.clinicaldecision.entity.TriggerEvent;
import com.healthcare.clinic.clinicaldecision.repository.CdsRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CdsRuleService {

    private final CdsRuleRepository ruleRepository;

    @Transactional
    public CdsRule createOrUpdateRule(CdsRule rule) {
        if (rule.getId() != null) {
            CdsRule existing = ruleRepository.findById(rule.getId())
                    .orElseThrow(() -> new RuntimeException("CDS Rule not found: " + rule.getId()));
            existing.setName(rule.getName());
            existing.setDescription(rule.getDescription());
            existing.setTriggerEvent(rule.getTriggerEvent());
            existing.setConditions(rule.getConditions());
            existing.setSeverity(rule.getSeverity());
            existing.setActionType(rule.getActionType());
            existing.setIsActive(rule.getIsActive());
            existing.setVersion(existing.getVersion() + 1);
            return ruleRepository.save(existing);
        }
        return ruleRepository.save(rule);
    }

    @Transactional(readOnly = true)
    public List<CdsRule> getActiveRules() {
        return ruleRepository.findByIsActiveTrue();
    }

    /**
     * Inline evaluation of rules against a composition context (advisory only for UI warnings).
     */
    @Transactional(readOnly = true)
    public Map<String, Object> evaluateRules(Long patientId, TriggerEvent triggerEvent, List<String> items) {
        List<CdsRule> matchingRules = ruleRepository.findByTriggerEventAndIsActiveTrue(triggerEvent);
        List<Map<String, Object>> triggered = new ArrayList<>();

        for (CdsRule rule : matchingRules) {
            Map<String, Object> match = new HashMap<>();
            match.put("ruleId", rule.getId());
            match.put("ruleName", rule.getName());
            match.put("severity", rule.getSeverity());
            match.put("actionType", rule.getActionType());
            match.put("message", "Rule '" + rule.getName() + "' matches evaluating context: " + items);
            triggered.add(match);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("patientId", patientId);
        response.put("triggerEvent", triggerEvent);
        response.put("evaluatedRulesCount", matchingRules.size());
        response.put("triggeredAlerts", triggered);
        return response;
    }
}
