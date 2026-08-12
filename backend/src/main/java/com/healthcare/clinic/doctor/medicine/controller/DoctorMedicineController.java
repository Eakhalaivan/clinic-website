package com.healthcare.clinic.doctor.medicine.controller;

import com.healthcare.clinic.doctor.medicine.dto.DoctorMedicineDto;
import com.healthcare.clinic.doctor.medicine.service.DoctorMedicineService;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.doctor.repository.DoctorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/doctor/medicines")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_DOCTOR')")
public class DoctorMedicineController {

    private final DoctorMedicineService doctorMedicineService;
    private final DoctorProfileRepository doctorProfileRepository;

    @GetMapping
    public List<DoctorMedicineDto> getMyMedicines(@AuthenticationPrincipal User user) {
        Long doctorProfileId = getDoctorProfileId(user);
        return doctorMedicineService.getMedicinesByDoctorId(doctorProfileId);
    }

    @PostMapping
    public DoctorMedicineDto createMedicine(@AuthenticationPrincipal User user, @RequestBody DoctorMedicineDto dto) {
        Long doctorProfileId = getDoctorProfileId(user);
        return doctorMedicineService.createMedicine(doctorProfileId, dto);
    }

    @PutMapping("/{id}")
    public DoctorMedicineDto updateMedicine(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody DoctorMedicineDto dto) {
        Long doctorProfileId = getDoctorProfileId(user);
        return doctorMedicineService.updateMedicine(doctorProfileId, id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteMedicine(@AuthenticationPrincipal User user, @PathVariable Long id) {
        Long doctorProfileId = getDoctorProfileId(user);
        doctorMedicineService.deleteMedicine(doctorProfileId, id);
    }

    private Long getDoctorProfileId(User user) {
        return doctorProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor profile not found"))
                .getId();
    }
}
