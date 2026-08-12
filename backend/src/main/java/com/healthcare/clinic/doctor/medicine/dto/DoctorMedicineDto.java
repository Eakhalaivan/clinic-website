package com.healthcare.clinic.doctor.medicine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorMedicineDto {
    private Long id;
    private Long doctorId;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private String unit;
    private Integer stockQuantity;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
