package com.healthcare.clinic.doctor.medicine.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorMedicineChangedEvent {
    private final Long doctorId;
}
