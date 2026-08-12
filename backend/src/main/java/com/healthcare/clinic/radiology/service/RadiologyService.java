package com.healthcare.clinic.radiology.service;

import com.healthcare.clinic.billing.dto.InvoiceItemRequest;
import com.healthcare.clinic.billing.dto.InvoiceRequest;
import com.healthcare.clinic.billing.dto.InvoiceResponse;
import com.healthcare.clinic.billing.entity.Invoice;
import com.healthcare.clinic.billing.entity.ItemType;
import com.healthcare.clinic.billing.repository.InvoiceRepository;
import com.healthcare.clinic.billing.service.BillingService;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.radiology.entity.ImagingProcedure;
import com.healthcare.clinic.radiology.entity.ImagingRequest;
import com.healthcare.clinic.radiology.entity.RadiologyReport;
import com.healthcare.clinic.radiology.repository.ImagingProcedureRepository;
import com.healthcare.clinic.radiology.repository.ImagingRequestRepository;
import com.healthcare.clinic.radiology.repository.RadiologyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RadiologyService {

    private final ImagingProcedureRepository procedureRepository;
    private final ImagingRequestRepository requestRepository;
    private final RadiologyReportRepository reportRepository;
    private final BillingService billingService;
    private final InvoiceRepository invoiceRepository;
    private final ApplicationEventPublisher eventPublisher;

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
    public ImagingRequest bookPatientRequest(Long id, ZonedDateTime scheduledAt, User user) {
        ImagingRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        
        if (!request.getPatient().getUserId().equals(user.getId()) && !user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_SUPER_ADMIN"))) {
            throw new IllegalArgumentException("Forbidden");
        }
        
        request.setScheduledAt(scheduledAt);
        request.setStatus("SCHEDULED");
        return requestRepository.save(request);
    }

    @Transactional
    public ImagingRequest createRequest(ImagingRequest request) {
        // Prevent duplicates on the same day
        ZonedDateTime startOfDay = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault());
        if (requestRepository.existsByPatientIdAndProcedureIdAndRequestedAtGreaterThanEqual(
                request.getPatient().getId(), request.getProcedure().getId(), startOfDay)) {
            throw new IllegalArgumentException("A request for this procedure was already created today.");
        }

        request.setStatus("ORDERED");
        request = requestRepository.save(request);

        // Generate Invoice
        InvoiceItemRequest item = InvoiceItemRequest.builder()
                .description("Radiology: " + request.getProcedure().getName())
                .quantity(1)
                .unitPrice(request.getProcedure().getPrice() != null ? request.getProcedure().getPrice() : BigDecimal.ZERO)
                .itemType(ItemType.RADIOLOGY)
                .referenceId(request.getId())
                .build();

        InvoiceRequest invoiceRequest = InvoiceRequest.builder()
                .patientId(request.getPatient().getUserId())
                .branchId(request.getBranch() != null ? request.getBranch().getId() : null)
                .appointmentId(null)
                .description("Radiology Request #" + request.getId())
                .dueDate(LocalDateTime.now().plusDays(30))
                .items(List.of(item))
                .build();

        try {
            InvoiceResponse invoiceResponse = billingService.createInvoice(invoiceRequest);
            Invoice invoice = invoiceRepository.findById(invoiceResponse.getId()).orElse(null);
            request.setInvoice(invoice);
            request = requestRepository.save(request);
        } catch (Exception e) {
            System.err.println("Error creating invoice for radiology request: " + e.getMessage());
            e.printStackTrace();
            // Log but don't fail the request creation
        }

        return request;
    }

    @Transactional
    public ImagingRequest updateRequestStatus(Long requestId, String newStatus) {
        ImagingRequest request = requestRepository.findById(requestId).orElseThrow();
        String currentStatus = request.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalStateException("Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        request.setStatus(newStatus);
        
        if ("SCHEDULED".equals(newStatus) && request.getScheduledAt() == null) {
            request.setScheduledAt(ZonedDateTime.now());
        }

        if ("CANCELLED".equals(newStatus) && request.getInvoice() != null) {
            try {
                billingService.cancelInvoice(request.getInvoice().getId());
            } catch (Exception e) {
                // Ignore billing error on cancellation
            }
        }

        return requestRepository.save(request);
    }

    private boolean isValidTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals(newStatus)) return true;
        return switch (currentStatus) {
            case "DRAFT" -> List.of("ORDERED", "CANCELLED").contains(newStatus);
            case "ORDERED" -> List.of("SCHEDULED", "IMAGE_ACQUIRED", "CANCELLED").contains(newStatus);
            case "SCHEDULED" -> List.of("IMAGE_ACQUIRED", "CANCELLED").contains(newStatus);
            case "IMAGE_ACQUIRED" -> List.of("REPORTING").contains(newStatus);
            case "REPORTING" -> List.of("VERIFIED").contains(newStatus);
            case "VERIFIED" -> List.of("RELEASED").contains(newStatus);
            default -> false;
        };
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

        if ("VERIFIED".equals(report.getStatus())) {
            throw new IllegalStateException("Cannot edit a verified report. Create an addendum instead.");
        }

        report.setFindings(reportInput.getFindings());
        report.setImpression(reportInput.getImpression());
        report.setDicomStudyUid(reportInput.getDicomStudyUid());
        report.setDicomImageUrl(reportInput.getDicomImageUrl());
        report.setStructuredData(reportInput.getStructuredData());
        
        if (reportInput.getStatus() != null) {
            if ("VERIFIED".equals(reportInput.getStatus())) {
                report.setStatus("VERIFIED");
                report.setVerifiedAt(ZonedDateTime.now());
                report.setVerifiedBy(radiologist);
                updateRequestStatus(requestId, "VERIFIED");
                eventPublisher.publishEvent("RadiologyReportVerified:" + report.getId());
            } else if ("FINALIZED".equals(reportInput.getStatus())) {
                report.setStatus("FINALIZED");
                report.setFinalizedAt(ZonedDateTime.now());
                updateRequestStatus(requestId, "RELEASED"); // Legacy compatibility or automatic release
                eventPublisher.publishEvent("RadiologyReportReleased:" + report.getId());
            } else {
                report.setStatus(reportInput.getStatus());
            }
        }
        return reportRepository.save(report);
    }
}
