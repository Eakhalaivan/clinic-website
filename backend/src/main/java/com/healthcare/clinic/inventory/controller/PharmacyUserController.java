package com.healthcare.clinic.inventory.controller;

import com.healthcare.clinic.common.dto.ApiResponse;
import com.healthcare.clinic.inventory.dto.CreateUserRequest;
import com.healthcare.clinic.inventory.dto.UserRequestDto;
import com.healthcare.clinic.inventory.dto.UserResponseDTO;
import com.healthcare.clinic.inventory.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController("pharmacyUserController")
@RequestMapping("/api/pharmacy/auth/users")
public class PharmacyUserController {

    private final UserService userService;

    public PharmacyUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_SYSTEM_ADMIN', 'ROLE_PHARMACIST')")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(), "Users fetched successfully"));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.createUser(request), "User created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(@PathVariable Long id, @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateUser(id, request), "User updated successfully"));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.toggleStatus(id), "User status toggled successfully"));
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, String>>> resetPassword(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.resetPassword(id), "Password reset successfully"));
    }

    @PutMapping("/{id}/profile")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_SYSTEM_ADMIN', 'ROLE_PHARMACIST', 'ROLE_STAFF')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateProfile(
            @PathVariable Long id,
            @RequestBody Map<String, String> profileData) {
        
        String name = profileData.get("name");
        String email = profileData.get("email");
        String phone = profileData.get("phone");
        String branch = profileData.get("location"); // map location from frontend to branch
        String shift = profileData.get("shift");
        
        UserResponseDTO updated = userService.updateProfile(id, name, email, phone, branch, shift);
        return ResponseEntity.ok(ApiResponse.success(updated, "Profile updated successfully"));
    }
}
