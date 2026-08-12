package com.healthcare.clinic.doctor.medicine.service;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.doctor.medicine.dto.DoctorMedicineDto;
import com.healthcare.clinic.doctor.medicine.entity.DoctorMedicine;
import com.healthcare.clinic.doctor.medicine.event.DoctorMedicineChangedEvent;
import com.healthcare.clinic.doctor.medicine.repository.DoctorMedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorMedicineService {

    private final DoctorMedicineRepository doctorMedicineRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<DoctorMedicineDto> getMedicinesByDoctorId(Long doctorId) {
        return doctorMedicineRepository.findAllByDoctorId(doctorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DoctorMedicineDto createMedicine(Long doctorId, DoctorMedicineDto dto) {
        DoctorMedicine medicine = DoctorMedicine.builder()
                .doctor(DoctorProfile.builder().id(doctorId).build())
                .name(dto.getName())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .price(dto.getPrice())
                .unit(dto.getUnit())
                .stockQuantity(dto.getStockQuantity())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();

        DoctorMedicine saved = doctorMedicineRepository.save(medicine);
        eventPublisher.publishEvent(new DoctorMedicineChangedEvent(doctorId));
        return mapToDto(saved);
    }

    @Transactional
    public DoctorMedicineDto updateMedicine(Long doctorId, Long medicineId, DoctorMedicineDto dto) {
        DoctorMedicine medicine = getDoctorMedicine(doctorId, medicineId);

        medicine.setName(dto.getName());
        medicine.setDescription(dto.getDescription());
        medicine.setImageUrl(dto.getImageUrl());
        medicine.setPrice(dto.getPrice());
        medicine.setUnit(dto.getUnit());
        medicine.setStockQuantity(dto.getStockQuantity());
        if (dto.getIsActive() != null) {
            medicine.setIsActive(dto.getIsActive());
        }

        DoctorMedicine saved = doctorMedicineRepository.save(medicine);
        eventPublisher.publishEvent(new DoctorMedicineChangedEvent(doctorId));
        return mapToDto(saved);
    }

    @Transactional
    public void deleteMedicine(Long doctorId, Long medicineId) {
        DoctorMedicine medicine = getDoctorMedicine(doctorId, medicineId);
        doctorMedicineRepository.delete(medicine);
        eventPublisher.publishEvent(new DoctorMedicineChangedEvent(doctorId));
    }

    private DoctorMedicine getDoctorMedicine(Long doctorId, Long medicineId) {
        DoctorMedicine medicine = doctorMedicineRepository.findById(medicineId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicine not found"));
        
        if (!medicine.getDoctor().getId().equals(doctorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to modify this medicine");
        }
        return medicine;
    }

    private DoctorMedicineDto mapToDto(DoctorMedicine entity) {
        return DoctorMedicineDto.builder()
                .id(entity.getId())
                .doctorId(entity.getDoctor().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .price(entity.getPrice())
                .unit(entity.getUnit())
                .stockQuantity(entity.getStockQuantity())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
