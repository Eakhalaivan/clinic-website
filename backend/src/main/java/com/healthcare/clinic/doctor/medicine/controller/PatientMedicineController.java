package com.healthcare.clinic.doctor.medicine.controller;

import com.healthcare.clinic.doctor.medicine.dto.DoctorMedicineDto;
import com.healthcare.clinic.doctor.medicine.dto.MedicineOrderRequest;
import com.healthcare.clinic.doctor.medicine.service.PatientMedicineService;
import com.healthcare.clinic.finance.service.StripePaymentService;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient/medicines")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_PATIENT')")
public class PatientMedicineController {

    private final PatientMedicineService patientMedicineService;
    private final PatientProfileRepository patientProfileRepository;
    private final StripePaymentService stripePaymentService;

    @GetMapping
    public List<DoctorMedicineDto> getAvailableMedicines(@AuthenticationPrincipal User user) {
        return patientMedicineService.getAvailableMedicines(user.getId());
    }

    @PostMapping("/order")
    public Map<String, String> orderMedicines(@AuthenticationPrincipal User user, @RequestBody MedicineOrderRequest request) {
        Long patientProfileId = getPatientProfileId(user);
        
        Long orderId = patientMedicineService.createOrder(patientProfileId, request);
        
        String checkoutUrl = stripePaymentService.createMedicineCheckoutSession(orderId);
        
        return Map.of("checkoutUrl", checkoutUrl);
    }

    private Long getPatientProfileId(User user) {
        return patientProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient profile not found"))
                .getId();
    }
}
