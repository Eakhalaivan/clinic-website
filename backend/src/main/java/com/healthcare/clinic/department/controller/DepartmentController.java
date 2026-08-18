package com.healthcare.clinic.department.controller;

import com.healthcare.clinic.department.entity.Department;
import com.healthcare.clinic.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<Page<Department>> getAllDepartments(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(departmentRepository.findAll(pageable));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @com.healthcare.clinic.audit.annotation.AuditableAction(module = "DEPARTMENT", action = "CREATE", resourceType = "Department", sensitivityLevel = "NORMAL")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        if (department.getIsActive() == null) department.setIsActive(true);
        return ResponseEntity.ok(departmentRepository.save(department));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @com.healthcare.clinic.audit.annotation.AuditableAction(module = "DEPARTMENT", action = "UPDATE", resourceType = "Department", sensitivityLevel = "NORMAL")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department request) {
        return departmentRepository.findById(id).map(dept -> {
            if (request.getName() != null) dept.setName(request.getName());
            if (request.getDescription() != null) dept.setDescription(request.getDescription());
            if (request.getHeadDoctorId() != null) dept.setHeadDoctorId(request.getHeadDoctorId());
            if (request.getIsActive() != null) dept.setIsActive(request.getIsActive());
            return ResponseEntity.ok(departmentRepository.save(dept));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @com.healthcare.clinic.audit.annotation.AuditableAction(module = "DEPARTMENT", action = "DELETE", resourceType = "Department", sensitivityLevel = "NORMAL")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
