package com.healthcare.clinic.surgery.dto;

import lombok.Data;
import java.util.Map;

@Data
public class PreOpChecklistRequest {
    private Map<String, Boolean> checklistData;
    private String notes;
}
