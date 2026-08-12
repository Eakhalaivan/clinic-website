package com.healthcare.clinic.radiology.service;

import com.healthcare.clinic.radiology.entity.ImagingRequest;
import com.healthcare.clinic.radiology.entity.RadiologyAppointment;
import com.healthcare.clinic.radiology.repository.ImagingRequestRepository;
import com.healthcare.clinic.radiology.repository.RadiologyAppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RadiologySchedulingService {

    private final RadiologyAppointmentRepository appointmentRepository;
    private final ImagingRequestRepository requestRepository;

    @Transactional
    public RadiologyAppointment scheduleAppointment(Long requestId, Long branchId, String roomOrMachine, ZonedDateTime scheduledTime) {
        ImagingRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Imaging Request not found: " + requestId));

        if (!"ORDERED".equals(request.getStatus()) && !"AWAITING_PAYMENT".equals(request.getStatus()) && !"DRAFT".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot schedule request in status: " + request.getStatus());
        }

        int durationMinutes = request.getProcedure().getDurationMinutes();
        ZonedDateTime endTime = scheduledTime.plusMinutes(durationMinutes);

        List<RadiologyAppointment> overlapping = appointmentRepository.findOverlappingAppointments(
                branchId, roomOrMachine, scheduledTime, endTime);

        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Slot is not available due to overlapping appointments on " + roomOrMachine);
        }

        RadiologyAppointment appointment = RadiologyAppointment.builder()
                .request(request)
                .patient(request.getPatient())
                .branch(request.getBranch())
                .modality(request.getProcedure().getModality())
                .scheduledTime(scheduledTime)
                .durationMinutes(durationMinutes)
                .roomOrMachine(roomOrMachine)
                .status("SCHEDULED")
                .build();

        appointment = appointmentRepository.save(appointment);

        request.setStatus("SCHEDULED");
        request.setScheduledAt(scheduledTime);
        requestRepository.save(request);

        return appointment;
    }

    @Transactional
    public RadiologyAppointment checkInPatient(Long appointmentId) {
        RadiologyAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appointmentId));

        if (!"SCHEDULED".equals(appointment.getStatus())) {
            throw new IllegalStateException("Only SCHEDULED appointments can be checked in.");
        }

        appointment.setStatus("CHECKED_IN");
        
        ImagingRequest request = appointment.getRequest();
        request.setStatus("CHECKED_IN");
        requestRepository.save(request);

        return appointmentRepository.save(appointment);
    }
}
