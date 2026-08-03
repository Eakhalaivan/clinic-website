package com.healthcare.clinic.doctor.dto;

import com.healthcare.clinic.doctor.entity.DoctorProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DoctorProfileWithNameDto {
    private Long id;
    private Long userId;
    private String specialty;
    private String qualifications;
    private Integer experienceYears;
    private BigDecimal consultationFee;
    private String bio;
    private Boolean isActive;
    private Long branchId;
    private String firstName;
    private String lastName;

    public DoctorProfileWithNameDto(DoctorProfile profile, String firstName, String lastName) {
        this.id = profile.getId();
        this.userId = profile.getUserId();
        this.specialty = profile.getSpecialty();
        this.qualifications = profile.getQualifications();
        this.experienceYears = profile.getExperienceYears();
        this.consultationFee = profile.getConsultationFee();
        this.bio = profile.getBio();
        this.isActive = profile.getIsActive();
        this.branchId = profile.getBranchId();
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
