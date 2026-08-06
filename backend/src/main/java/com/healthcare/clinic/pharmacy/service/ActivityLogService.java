package com.healthcare.clinic.pharmacy.service;

import com.healthcare.clinic.pharmacy.entity.ActivityLog;
import com.healthcare.clinic.pharmacy.repository.ActivityLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Service layer for Activity Log queries.
 * Extracted from ActivityLogController to enforce the single-responsibility principle.
 */
@Service("pharmacyActivityLogService")
@Transactional(readOnly = true)
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    /**
     * Returns paginated activity logs for a user, optionally filtered to today only.
     *
     * @param userId  The user whose logs to retrieve.
     * @param dateFilter Pass {@code "today"} to restrict to today's logs; {@code null} for all.
     * @param page    Zero-based page index.
     * @param size    Page size.
     * @return A page of {@link ActivityLog} records ordered by creation date descending.
     */
    public Page<ActivityLog> getLogs(Long userId, String dateFilter, Pageable pageable) {
        if ("today".equals(dateFilter)) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay   = LocalDateTime.now();
            return activityLogRepository
                    .findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, startOfDay, endOfDay, pageable);
        }
        return activityLogRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
}
