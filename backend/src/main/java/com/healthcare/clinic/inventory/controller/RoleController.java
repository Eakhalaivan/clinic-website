package com.healthcare.clinic.inventory.controller;

import com.healthcare.clinic.inventory.entity.PharmacyRole;
import com.healthcare.clinic.inventory.repository.PharmacyRoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.healthcare.clinic.common.dto.ApiResponse;

import java.util.List;

@RestController("pharmacyRoleController")
@RequestMapping("/api/pharmacy/auth/roles")
public class RoleController {

    private final PharmacyRoleRepository roleRepository;

    public RoleController(PharmacyRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<List<PharmacyRole>>> getAllRoles() {
        return ResponseEntity.ok(ApiResponse.success(roleRepository.findAll(), "Roles fetched successfully"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<PharmacyRole>> createRole(@RequestBody PharmacyRole role) {
        if (role.getIsSystemDefault() == null) {
            role.setIsSystemDefault(false);
        }
        return ResponseEntity.ok(ApiResponse.success(roleRepository.save(role), "PharmacyRole created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<PharmacyRole>> updateRole(@PathVariable Long id, @RequestBody PharmacyRole updatedRole) {
        return roleRepository.findById(id).map(role -> {
            role.setName(updatedRole.getName());
            role.setColor(updatedRole.getColor());
            role.setPermissionsJson(updatedRole.getPermissionsJson());
            return ResponseEntity.ok(ApiResponse.success(roleRepository.save(role), "PharmacyRole updated successfully"));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        return roleRepository.findById(id).map(role -> {
            if (Boolean.TRUE.equals(role.getIsSystemDefault())) {
                return ResponseEntity.badRequest().body(ApiResponse.<Void>error("Cannot delete system default role"));
            }
            roleRepository.delete(role);
            return ResponseEntity.ok(ApiResponse.<Void>success(null, "PharmacyRole deleted successfully"));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
