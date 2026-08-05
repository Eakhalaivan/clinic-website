package com.healthcare.clinic.config;

import com.healthcare.clinic.identity.entity.Role;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.RoleRepository;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.doctor.entity.DoctorProfile;
import com.healthcare.clinic.doctor.repository.DoctorProfileRepository;
import com.healthcare.clinic.doctor.entity.DoctorWorkingHours;
import com.healthcare.clinic.doctor.repository.DoctorWorkingHoursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Profile;

/**
 * Seeds initial admin and doctor accounts on first boot.
 *
 * Requires SEED_ADMIN_PASSWORD and SEED_DOCTOR_PASSWORD to be set in the
 * environment (or application properties).  The application will refuse to
 * start if either value is still the default CHANGE_ME sentinel, preventing
 * weak default credentials from reaching production.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private static final String SENTINEL_PREFIX = "CHANGE_ME_";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorWorkingHoursRepository doctorWorkingHoursRepository;

    @Value("${SEED_ADMIN_PASSWORD:AdminPass123!}")
    private String seedAdminPassword;

    @Value("${SEED_DOCTOR_PASSWORD:DoctorPass123!}")
    private String seedDoctorPassword;

    @Override
    public void run(String... args) throws Exception {
        validateSeedPassword("SEED_ADMIN_PASSWORD", seedAdminPassword);
        validateSeedPassword("SEED_DOCTOR_PASSWORD", seedDoctorPassword);

        // Ensure all system roles exist in DB
        String[] allRoleNames = {
            "ROLE_ADMIN", "ROLE_SUPER_ADMIN", "ROLE_SYSTEM_ADMIN", "ROLE_BRANCH_ADMIN",
            "ROLE_DOCTOR", "ROLE_PATIENT", "ROLE_PHARMACIST", "ROLE_NURSE",
            "ROLE_LAB", "ROLE_LAB_TECH",
            "ROLE_RADIOLOGIST", "ROLE_RECEPTION", "ROLE_FINANCE", "ROLE_ACCOUNTANT",
            "ROLE_INVENTORY_MANAGER", "ROLE_MARKETING", "ROLE_STORE_MANAGER", "ROLE_SUPPORT",
            "ROLE_CUSTOMER_SUPPORT", "ROLE_VENDOR", "ROLE_INSURANCE", "ROLE_AMBULANCE", "ROLE_HR"
        };

        Set<Role> adminRoles = new HashSet<>();
        for (String roleName : allRoleNames) {
            Role role = roleRepository.findByName(roleName).orElseGet(() -> {
                Role newRole = Role.builder().name(roleName).build();
                newRole.setLoginPortal(getLoginPortalForRole(roleName));
                return roleRepository.save(newRole);
            });
            adminRoles.add(role);
        }

        // Seed / Reset Admin User with Full Roles Access
        User admin = userRepository.findByEmail("admin@clinic.com").orElseGet(() -> 
            User.builder().email("admin@clinic.com").firstName("Admin").lastName("User").build()
        );
        admin.setPasswordHash(passwordEncoder.encode(seedAdminPassword));
        admin.setFailedLoginAttempts(0);
        admin.setLockedUntil(null);
        admin.setEnabled(true);
        admin.setRoles(adminRoles);
        userRepository.save(admin);
        log.info("DataSeeder: synced admin@clinic.com with full administrative and system roles.");

        // Seed / Reset Doctor User
        User doctor = userRepository.findByEmail("doctor@clinic.com").orElseGet(() -> 
            User.builder().email("doctor@clinic.com").firstName("John").lastName("Doe").build()
        );
        doctor.setPasswordHash(passwordEncoder.encode(seedDoctorPassword));
        doctor.setFailedLoginAttempts(0);
        doctor.setLockedUntil(null);
        doctor.setEnabled(true);

        Set<Role> doctorRoles = doctor.getRoles() != null ? new HashSet<>(doctor.getRoles()) : new HashSet<>();
        roleRepository.findByName("ROLE_DOCTOR").ifPresent(doctorRoles::add);
        doctor.setRoles(doctorRoles);
        userRepository.save(doctor);
        log.info("DataSeeder: synced doctor@clinic.com credentials and roles.");

        // Ensure the seeded doctor has a DoctorProfile so the dashboard loads
        if (doctorProfileRepository.findByUserId(doctor.getId()).isEmpty()) {
            DoctorProfile profile = DoctorProfile.builder()
                    .userId(doctor.getId())
                    .specialty("General Medicine")
                    .qualifications("MBBS, MD")
                    .experienceYears(5)
                    .consultationFee(new java.math.BigDecimal("500.00"))
                    .bio("Experienced general practitioner with a focus on preventive care.")
                    .isActive(true)
                    .branchId(1L)
                    .build();
            doctorProfileRepository.save(profile);
            log.info("DataSeeder: created default DoctorProfile for doctor@clinic.com.");
        }
        
        DoctorProfile savedProfile = doctorProfileRepository.findByUserId(doctor.getId()).orElse(null);
        if (savedProfile != null && doctorWorkingHoursRepository.findByDoctorIdAndIsActiveTrue(savedProfile.getId()).isEmpty()) {
            for (int day = 1; day <= 5; day++) { // Monday to Friday
                DoctorWorkingHours hours = DoctorWorkingHours.builder()
                        .doctor(savedProfile)
                        .dayOfWeek(day)
                        .startTime(java.time.LocalTime.of(9, 0))
                        .endTime(java.time.LocalTime.of(17, 0))
                        .slotDurationMinutes(20)
                        .isActive(true)
                        .branchId(savedProfile.getBranchId())
                        .build();
                doctorWorkingHoursRepository.save(hours);
            }
            log.info("DataSeeder: created default DoctorWorkingHours for doctor@clinic.com.");
        }

        // Seed / Reset Nurse User
        User nurse = userRepository.findByEmail("nurse@clinic.com").orElseGet(() -> 
            User.builder().email("nurse@clinic.com").firstName("Jane").lastName("Smith").build()
        );
        nurse.setPasswordHash(passwordEncoder.encode("NursePass123!"));
        nurse.setFailedLoginAttempts(0);
        nurse.setLockedUntil(null);
        nurse.setEnabled(true);

        Set<Role> nurseRoles = nurse.getRoles() != null ? new HashSet<>(nurse.getRoles()) : new HashSet<>();
        roleRepository.findByName("ROLE_NURSE").ifPresent(nurseRoles::add);
        nurse.setRoles(nurseRoles);
        userRepository.save(nurse);
        log.info("DataSeeder: synced nurse@clinic.com credentials and roles.");
    }

    private void validateSeedPassword(String envVarName, String value) {
        if (value == null || value.startsWith(SENTINEL_PREFIX)) {
            throw new IllegalStateException(
                "Refusing to start: " + envVarName + " is not configured. " +
                "Set a strong password in your .env file before running the application."
            );
        }
    }

    private String getLoginPortalForRole(String roleName) {
        return switch (roleName) {
            case "ROLE_PATIENT" -> "patient";
            case "ROLE_DOCTOR" -> "doctor";
            case "ROLE_ADMIN", "ROLE_SYSTEM_ADMIN", "ROLE_SUPER_ADMIN" -> "admin";
            case "ROLE_BRANCH_ADMIN" -> "branch-admin";
            case "ROLE_NURSE" -> "nurse";
            case "ROLE_RECEPTION" -> "reception";
            case "ROLE_PHARMACIST" -> "pharmacist";
            case "ROLE_LAB_TECH", "ROLE_LAB" -> "lab";
            case "ROLE_RADIOLOGIST" -> "radiologist";
            case "ROLE_ACCOUNTANT" -> "accountant";
            case "ROLE_FINANCE" -> "finance";
            case "ROLE_INVENTORY_MANAGER", "ROLE_STORE_MANAGER" -> "inventory";
            case "ROLE_MARKETING" -> "marketing";
            case "ROLE_SUPPORT", "ROLE_CUSTOMER_SUPPORT" -> "customer-support";
            case "ROLE_VENDOR" -> "vendor";
            case "ROLE_INSURANCE" -> "insurance";
            case "ROLE_AMBULANCE" -> "ambulance";
            case "ROLE_HR" -> "hr";
            default -> null;
        };
    }
}
