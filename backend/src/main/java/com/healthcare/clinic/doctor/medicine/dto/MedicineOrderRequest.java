package com.healthcare.clinic.doctor.medicine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineOrderRequest {
    private Long doctorId;
    private List<MedicineCartItemDto> items;
}
