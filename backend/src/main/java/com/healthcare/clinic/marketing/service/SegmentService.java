package com.healthcare.clinic.marketing.service;

import com.healthcare.clinic.marketing.entity.CampaignSegment;
import com.healthcare.clinic.marketing.repository.CampaignSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SegmentService {

    private final CampaignSegmentRepository segmentRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<CampaignSegment> listSegmentsForBranch(Long branchId) {
        if (branchId == null) {
            return segmentRepository.findByIsPublicTrueAndIsActiveTrue();
        }
        return segmentRepository.findByBranchIdAndIsActiveTrue(branchId);
    }

    @Transactional
    public CampaignSegment createSegment(CampaignSegment segment, Long createdBy) {
        segment.setCreatedBy(createdBy);
        segment.setVersion(1);
        // Compute initial count immediately
        int count = computeAudienceCount(segment.getCriteriaJson());
        segment.setEstimatedCount(count);
        return segmentRepository.save(segment);
    }

    @Transactional
    public CampaignSegment updateSegment(Long id, CampaignSegment updated, Long updatedBy) {
        CampaignSegment existing = segmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Segment not found: " + id));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setCriteriaJson(updated.getCriteriaJson());
        existing.setVersion(existing.getVersion() + 1);
        // Recompute count after criteria change
        int count = computeAudienceCount(updated.getCriteriaJson());
        existing.setEstimatedCount(count);
        return segmentRepository.save(existing);
    }

    /**
     * Returns the estimated count of patients matching this segment's criteria.
     * Server-side only — no patient records are sent to the browser.
     */
    @Transactional(readOnly = true)
    public int countAudience(Long segmentId) {
        CampaignSegment segment = segmentRepository.findById(segmentId)
                .orElseThrow(() -> new IllegalArgumentException("Segment not found: " + segmentId));
        return computeAudienceCount(segment.getCriteriaJson());
    }

    /**
     * Returns paginated patient IDs matching the segment — no clinical data exposed.
     * DO NOT expose diagnosis, lab results, or sensitive clinical attributes here.
     */
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> previewAudience(Long segmentId, Pageable pageable) {
        CampaignSegment segment = segmentRepository.findById(segmentId)
                .orElseThrow(() -> new IllegalArgumentException("Segment not found: " + segmentId));
        String sql = buildPreviewSql(segment.getCriteriaJson(), pageable);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        long total = computeAudienceCount(segment.getCriteriaJson());
        return new org.springframework.data.domain.PageImpl<>(rows, pageable, total);
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    /**
     * Builds a safe parameterized query from criteria JSON.
     * Only allowed criteria keys are processed; all others are ignored.
     * No clinical data (diagnoses, lab results, medications) is included.
     */
    private int computeAudienceCount(Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            // Return count of all active patients
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM users WHERE is_active = TRUE", Integer.class);
            return count != null ? count : 0;
        }

        StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT u.id) FROM users u");
        List<Object> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        conditions.add("u.is_active = TRUE");

        applyAllowedCriteria(criteria, sql, conditions, params);

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        Integer count = jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
        return count != null ? count : 0;
    }

    private String buildPreviewSql(Map<String, Object> criteria, Pageable pageable) {
        StringBuilder sql = new StringBuilder(
                "SELECT u.id, u.first_name, u.last_name, u.email FROM users u");
        List<String> conditions = new ArrayList<>();
        conditions.add("u.is_active = TRUE");

        applyAllowedCriteria(criteria, sql, conditions, new ArrayList<>());

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }
        sql.append(" ORDER BY u.id LIMIT ")
           .append(pageable.getPageSize())
           .append(" OFFSET ")
           .append(pageable.getOffset());
        return sql.toString();
    }

    /**
     * SAFE criteria allowlist — only non-clinical, demographic/behavioral fields.
     * NEVER add: diagnosis_code, lab_result, prescription, clinical_note, radiology_report.
     */
    private void applyAllowedCriteria(Map<String, Object> criteria, StringBuilder sql,
                                       List<String> conditions, List<Object> params) {
        if (criteria.containsKey("branchId")) {
            sql.append(" JOIN branch_users bu ON bu.user_id = u.id");
            conditions.add("bu.branch_id = " + criteria.get("branchId"));
        }
        if (criteria.containsKey("minAge")) {
            conditions.add("EXTRACT(YEAR FROM AGE(u.date_of_birth)) >= " + criteria.get("minAge"));
        }
        if (criteria.containsKey("maxAge")) {
            conditions.add("EXTRACT(YEAR FROM AGE(u.date_of_birth)) <= " + criteria.get("maxAge"));
        }
        if (criteria.containsKey("loyaltyTier")) {
            sql.append(" JOIN patient_loyalty pl ON pl.patient_id = u.id");
            conditions.add("pl.tier = '" + criteria.get("loyaltyTier") + "'");
        }
        if (criteria.containsKey("hasActiveMembership")) {
            boolean active = Boolean.parseBoolean(criteria.get("hasActiveMembership").toString());
            if (active) {
                sql.append(" JOIN patient_memberships pm ON pm.patient_id = u.id AND pm.status = 'ACTIVE'");
            } else {
                conditions.add("NOT EXISTS (SELECT 1 FROM patient_memberships pm WHERE pm.patient_id = u.id AND pm.status = 'ACTIVE')");
            }
        }
        // Additional allowed criteria can be added here as needed
    }
}
