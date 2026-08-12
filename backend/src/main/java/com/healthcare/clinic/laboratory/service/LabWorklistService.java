package com.healthcare.clinic.laboratory.service;

import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import com.healthcare.clinic.laboratory.repository.LabTestRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LabWorklistService {

    private final LabTestRequestRepository requestRepository;

    @Transactional(readOnly = true)
    public Page<LabTestRequest> getWorklist(
            String status,
            String priority,
            Long branchId,
            Long patientId,
            Long doctorId,
            String specimenType,
            String department,
            ZonedDateTime fromDate,
            ZonedDateTime toDate,
            String search,
            Pageable pageable) {

        Specification<LabTestRequest> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(status)) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (StringUtils.hasText(priority)) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }
            if (branchId != null) {
                predicates.add(cb.equal(root.get("branch").get("id"), branchId));
            }
            if (patientId != null) {
                predicates.add(cb.equal(root.get("patient").get("id"), patientId));
            }
            if (doctorId != null) {
                predicates.add(cb.equal(root.get("doctor").get("userId"), doctorId));
            }
            if (StringUtils.hasText(specimenType)) {
                predicates.add(cb.equal(root.get("testCatalog").get("specimenType"), specimenType));
            }
            if (StringUtils.hasText(department)) {
                predicates.add(cb.equal(root.get("testCatalog").get("department"), department));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("requestedAt"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("requestedAt"), toDate));
            }
            if (StringUtils.hasText(search)) {
                String likePattern = "%" + search.toLowerCase() + "%";
                Predicate patientName = cb.like(cb.lower(root.get("patient").get("firstName")), likePattern);
                Predicate patientLast = cb.like(cb.lower(root.get("patient").get("lastName")), likePattern);
                Predicate requestNumber = cb.like(cb.lower(root.get("labRequestNumber")), likePattern);
                Predicate barcode = cb.like(cb.lower(root.get("sampleBarcodeId")), likePattern);
                predicates.add(cb.or(patientName, patientLast, requestNumber, barcode));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // To prevent multiple bag fetch issues, we could rely on @EntityGraph, but Spring Data JPA
        // handles it automatically if we set the EntityGraph on the repository method or if we don't
        // have EAGER collections.
        return requestRepository.findAll(spec, pageable);
    }
}
