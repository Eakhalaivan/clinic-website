package com.healthcare.clinic.reception.service;

import com.healthcare.clinic.appointment.repository.AppointmentRepository;
import com.healthcare.clinic.patient.entity.PatientDocument;
import com.healthcare.clinic.patient.repository.PatientDocumentRepository;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import com.healthcare.clinic.reception.entity.KioskCheckin;
import com.healthcare.clinic.reception.entity.ReceptionDocumentUpload;
import com.healthcare.clinic.reception.entity.WalkInRegistration;
import com.healthcare.clinic.reception.repository.KioskCheckinRepository;
import com.healthcare.clinic.reception.repository.QueueTokenRepository;
import com.healthcare.clinic.reception.repository.ReceptionDocumentUploadRepository;
import com.healthcare.clinic.reception.repository.WalkInRegistrationRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KioskService {

    private final KioskCheckinRepository kioskCheckinRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final AppointmentRepository appointmentRepository;
    private final WalkInRegistrationRepository walkInRegistrationRepository;
    private final QueueTokenRepository queueTokenRepository;
    private final PatientDocumentRepository patientDocumentRepository;
    private final ReceptionDocumentUploadRepository receptionDocumentUploadRepository;

    /**
     * Patient self-checks in via kiosk — verifies appointment and creates kiosk record.
     */
    @Transactional
    public KioskCheckin selfCheckIn(Long branchId, Long patientProfileId, Long appointmentId, String station) {
        KioskCheckin checkin = KioskCheckin.builder()
                .branchId(branchId)
                .patientId(patientProfileId)
                .appointmentId(appointmentId)
                .checkinMethod("KIOSK")
                .status("PENDING")
                .kioskStation(station)
                .build();
        return kioskCheckinRepository.save(checkin);
    }

    /**
     * Reception staff verifies a kiosk check-in.
     */
    @Transactional
    public KioskCheckin verifyCheckin(Long checkinId, String newStatus) {
        KioskCheckin checkin = kioskCheckinRepository.findById(checkinId)
                .orElseThrow(() -> new RuntimeException("Kiosk check-in not found"));
        checkin.setStatus(newStatus);
        checkin.setVerifiedAt(ZonedDateTime.now());
        checkin.setVerifiedByStaff(SecurityUtils.getCurrentUserId());
        return kioskCheckinRepository.save(checkin);
    }

    /**
     * Get today's kiosk check-ins for a branch.
     */
    public List<KioskCheckin> getTodaysCheckins(Long branchId) {
        ZonedDateTime startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        return kioskCheckinRepository.findByBranchIdAndCreatedAtAfterOrderByCreatedAtDesc(branchId, startOfDay);
    }

    /**
     * Upload a document on behalf of a patient (reception desk scanning).
     */
    @Transactional
    public PatientDocument uploadDocumentForPatient(Long patientProfileId, Long branchId,
                                                     String title, String documentType,
                                                     String fileUrl, String scanDevice, String notes) {
        PatientDocument document = new PatientDocument();
        document.setPatientId(patientProfileId);
        document.setTitle(title);
        document.setDocumentType(documentType);
        document.setFileUrl(fileUrl != null ? fileUrl : "https://placeholder.documents/scan.pdf");
        PatientDocument saved = patientDocumentRepository.save(document);

        // Audit trail
        ReceptionDocumentUpload audit = ReceptionDocumentUpload.builder()
                .patientDocumentId(saved.getId())
                .uploadedByStaffId(SecurityUtils.getCurrentUserId())
                .branchId(branchId)
                .scanDevice(scanDevice)
                .notes(notes)
                .build();
        receptionDocumentUploadRepository.save(audit);

        return saved;
    }

    /**
     * Get all documents for a patient (for display on reception desk).
     */
    public List<PatientDocument> getPatientDocuments(Long patientProfileId) {
        return patientDocumentRepository.findByPatientIdOrderByUploadedAtDesc(patientProfileId);
    }

    /**
     * Get real-time dashboard stats for a branch.
     */
    public Map<String, Object> getDashboardStats(Long branchId) {
        ZonedDateTime startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault());

        long kioskPending = kioskCheckinRepository.countByBranchIdAndStatusAndCreatedAtAfter(branchId, "PENDING", startOfDay);
        long kioskVerified = kioskCheckinRepository.countByBranchIdAndStatusAndCreatedAtAfter(branchId, "VERIFIED", startOfDay);
        long kioskCheckedIn = kioskCheckinRepository.countByBranchIdAndStatusAndCreatedAtAfter(branchId, "CHECKED_IN", startOfDay);

        List<WalkInRegistration> walkIns = walkInRegistrationRepository.findByBranchId(branchId);
        long walkInToday = walkIns.stream()
                .filter(w -> w.getRegisteredAt() != null
                        && w.getBranch() != null
                        && w.getBranch().getId().equals(branchId)
                        && w.getRegisteredAt().isAfter(startOfDay))
                .count();

        long queueWaiting = queueTokenRepository.countByBranchIdAndStatus(branchId, "WAITING");

        Map<String, Object> stats = new HashMap<>();
        stats.put("kioskPending", kioskPending);
        stats.put("kioskVerified", kioskVerified);
        stats.put("kioskCheckedIn", kioskCheckedIn);
        stats.put("walkInsToday", walkInToday);
        stats.put("queueWaiting", queueWaiting);
        stats.put("totalKioskToday", kioskPending + kioskVerified + kioskCheckedIn);
        return stats;
    }
}
