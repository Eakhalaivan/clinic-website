package com.healthcare.clinic.identity.controller;

import com.healthcare.clinic.identity.dto.UserSummaryDto;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

        private final UserRepository userRepository;

        @GetMapping
        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
        public ResponseEntity<Page<UserSummaryDto>> getUsers(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                Page<User> users = userRepository.findAll(PageRequest.of(page, size));
                Page<UserSummaryDto> dtoPage = users.map(user -> UserSummaryDto.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .phone(user.getPhoneNumber())
                                .enabled(user.getEnabled() != null ? user.getEnabled() : true)
                                .roleNames(user.getRoles() != null
                                                ? user.getRoles().stream().map(role -> role.getName())
                                                                .collect(Collectors.toList())
                                                : java.util.Collections.emptyList())
                                .build());
                return ResponseEntity.ok(dtoPage);
        }

        @GetMapping("/search")
        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_HR_MANAGER')")
        public ResponseEntity<java.util.List<UserSummaryDto>> searchUsers(
                        @RequestParam(required = false, defaultValue = "") String q,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "20") int size) {
                Page<User> users = q.isBlank()
                        ? userRepository.findAll(PageRequest.of(page, size))
                        : userRepository.searchByNameOrEmail(q, PageRequest.of(page, size));
                java.util.List<UserSummaryDto> result = users.getContent().stream()
                        .map(user -> UserSummaryDto.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .phone(user.getPhoneNumber())
                                .enabled(user.getEnabled() != null ? user.getEnabled() : true)
                                .roleNames(user.getRoles() != null
                                        ? user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList())
                                        : java.util.Collections.emptyList())
                                .build())
                        .collect(Collectors.toList());
                return ResponseEntity.ok(result);
        }


        @PutMapping("/{id}")
        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
        public ResponseEntity<UserSummaryDto> updateUser(@PathVariable Long id, @RequestBody UserSummaryDto updateDto) {
                User user = userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("User not found: " + id));

                if (updateDto.getFirstName() != null)
                        user.setFirstName(updateDto.getFirstName());
                if (updateDto.getLastName() != null)
                        user.setLastName(updateDto.getLastName());
                if (updateDto.getPhone() != null)
                        user.setPhoneNumber(updateDto.getPhone());
                if (updateDto.getEmail() != null)
                        user.setEmail(updateDto.getEmail());
                user.setEnabled(updateDto.isEnabled());

                User saved = userRepository.save(user);

                return ResponseEntity.ok(UserSummaryDto.builder()
                                .id(saved.getId())
                                .email(saved.getEmail())
                                .firstName(saved.getFirstName())
                                .lastName(saved.getLastName())
                                .phone(saved.getPhoneNumber())
                                .enabled(saved.getEnabled() != null ? saved.getEnabled() : true)
                                .roleNames(saved.getRoles() != null
                                                ? saved.getRoles().stream().map(role -> role.getName())
                                                                .collect(Collectors.toList())
                                                : java.util.Collections.emptyList())
                                .build());
        }

        @PatchMapping("/{id}/toggle-status")
        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
        public ResponseEntity<Void> toggleUserStatus(@PathVariable Long id) {
                User user = userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("User not found: " + id));
                boolean current = user.getEnabled() != null ? user.getEnabled() : true;
                user.setEnabled(!current);
                userRepository.save(user);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_DOCTOR', 'ROLE_PATIENT')")
        public ResponseEntity<UserSummaryDto> getUserById(@PathVariable Long id) {
                User user = userRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("User not found: " + id));
                return ResponseEntity.ok(UserSummaryDto.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .phone(user.getPhoneNumber())
                                .enabled(user.getEnabled() != null ? user.getEnabled() : true)
                                .roleNames(user.getRoles() != null
                                                ? user.getRoles().stream().map(role -> role.getName())
                                                                .collect(Collectors.toList())
                                                : java.util.Collections.emptyList())
                                .build());
        }
}
