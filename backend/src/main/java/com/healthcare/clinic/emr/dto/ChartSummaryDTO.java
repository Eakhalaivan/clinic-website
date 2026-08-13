package com.healthcare.clinic.emr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.healthcare.clinic.emr.entity.Allergy;
import com.healthcare.clinic.emr.entity.Problem;
import com.healthcare.clinic.emr.entity.ExternalMedicationHistoryEntry;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartSummaryDTO {
    private Long patientId;
    private Integer age;
    private String bloodGroup;
    private String emergencyContactName;
    private String emergencyContactPhone;

    private List<Allergy> activeAllergies;
    private List<Problem> activeProblems;
    private List<ExternalMedicationHistoryEntry> currentMedications;
}
