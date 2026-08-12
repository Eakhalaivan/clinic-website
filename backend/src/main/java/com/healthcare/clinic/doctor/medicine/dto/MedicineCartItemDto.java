package com.healthcare.clinic.doctor.medicine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineCartItemDto {
    private Long doctorMedicineId;
    private Integer quantity;
}
