package com.healthcare.clinic.emergency.dto;

import lombok.Data;

@Data
public class TriageRequest {
    private String triageLevel;
    private String chiefComplaint;
}
