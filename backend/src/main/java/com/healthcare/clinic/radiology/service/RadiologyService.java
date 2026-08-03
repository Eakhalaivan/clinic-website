package com.healthcare.clinic.radiology.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.radiology.entity.ImagingProcedure;
import com.healthcare.clinic.radiology.entity.ImagingRequest;
import com.healthcare.clinic.radiology.entity.RadiologyReport;
import com.healthcare.clinic.radiology.repository.ImagingProcedureRepository;
import com.healthcare.clinic.radiology.repository.ImagingRequestRepository;
import com.healthcare.clinic.radiology.repository.RadiologyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RadiologyService {

    private final ImagingProcedureRepository procedureRepository;
    private final ImagingRequestRepository requestRepository;
    private final RadiologyReportRepository reportRepository;

    @Transactional(readOnly = true)
    public List<ImagingProcedure> getProcedures() {
        return procedureRepository.findByIsActiveTrue();
    }

    @Transactional
    public ImagingProcedure createProcedure(ImagingProcedure procedure) {
        return procedureRepository.save(procedure);
    }

    @Transactional(readOnly = true)
    public List<ImagingRequest> getAllRequests() {
        return requestRepository.findAllByOrderByRequestedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<ImagingRequest> getRequestsByStatus(String status) {
        return requestRepository.findByStatus(status);
    }

    @Transactional
    public ImagingRequest createRequest(ImagingRequest request) {
        return requestRepository.save(request);
    }

    @Transactional
    public ImagingRequest updateRequestStatus(Long requestId, String status) {
        ImagingRequest request = requestRepository.findById(requestId).orElseThrow();
        request.setStatus(status);
        if ("SCHEDULED".equals(status)) {
            request.setScheduledAt(ZonedDateTime.now());
        }
        return requestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public Optional<RadiologyReport> getReportByRequestId(Long requestId) {
        return reportRepository.findByRequestId(requestId);
    }

    @Transactional
    public RadiologyReport saveReport(Long requestId, RadiologyReport reportInput, User radiologist) {
        ImagingRequest request = requestRepository.findById(requestId).orElseThrow();
        RadiologyReport report = reportRepository.findByRequestId(requestId)
                .orElse(RadiologyReport.builder()
                        .request(request)
                        .radiologist(radiologist)
                        .build());

        report.setFindings(reportInput.getFindings());
        report.setImpression(reportInput.getImpression());
        report.setDicomStudyUid(reportInput.getDicomStudyUid());
        report.setDicomImageUrl(reportInput.getDicomImageUrl());
        if (reportInput.getStatus() != null) {
            report.setStatus(reportInput.getStatus());
            if ("FINALIZED".equals(reportInput.getStatus())) {
                report.setFinalizedAt(ZonedDateTime.now());
                request.setStatus("COMPLETED");
                requestRepository.save(request);
            }
        }
        return reportRepository.save(report);
    }
}
