package com.healthcare.clinic.identity.service;

import com.healthcare.clinic.identity.dto.UserCreateDto;
import com.healthcare.clinic.identity.dto.UserSummaryDto;
import com.healthcare.clinic.identity.entity.Role;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.RoleRepository;
import com.healthcare.clinic.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<String> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserSummaryDto createUser(UserCreateDto createDto) {
        checkPrivilegeEscalation(createDto.getRoleNames());

        User user = User.builder()
                .email(createDto.getEmail())
                .firstName(createDto.getFirstName())
                .lastName(createDto.getLastName())
                .phoneNumber(createDto.getPhone())
                .passwordHash(passwordEncoder.encode(createDto.getPassword()))
                .enabled(createDto.isEnabled())
                .build();

        if (createDto.getRoleNames() != null && !createDto.getRoleNames().isEmpty()) {
            Set<Role> roles = createDto.getRoleNames().stream()
                    .map(name -> roleRepository.findByName(name)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        User saved = userRepository.save(user);
        return mapToSummaryDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<UserSummaryDto> getUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).map(this::mapToSummaryDto);
    }

    @Transactional(readOnly = true)
    public List<UserSummaryDto> searchUsers(String q, int page, int size) {
        Page<User> users = (q == null || q.isBlank())
                ? userRepository.findAll(PageRequest.of(page, size))
                : userRepository.searchByNameOrEmail(q, PageRequest.of(page, size));
        return users.getContent().stream().map(this::mapToSummaryDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserSummaryDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        checkIdorAccess(user);
        return mapToSummaryDto(user);
    }

    @Transactional
    public UserSummaryDto updateUser(Long id, UserSummaryDto updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        checkIdorAccess(user);

        if (updateDto.getRoleNames() != null) {
            checkPrivilegeEscalation(updateDto.getRoleNames());
            // Prevent ADMIN from removing SUPER_ADMIN from existing super admin
            boolean isTargetSuperAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_SUPER_ADMIN"));
            boolean isCurrentUserSuperAdmin = isCurrentUserSuperAdmin();
            if (isTargetSuperAdmin && !isCurrentUserSuperAdmin) {
                throw new AccessDeniedException("ADMIN cannot modify a SUPER_ADMIN user");
            }
        }

        if (updateDto.getFirstName() != null) user.setFirstName(updateDto.getFirstName());
        if (updateDto.getLastName() != null) user.setLastName(updateDto.getLastName());
        if (updateDto.getPhone() != null) user.setPhoneNumber(updateDto.getPhone());
        if (updateDto.getEmail() != null) user.setEmail(updateDto.getEmail());
        user.setEnabled(updateDto.isEnabled());

        if (updateDto.getRoleNames() != null) {
            Set<Role> roles = updateDto.getRoleNames().stream()
                    .map(name -> roleRepository.findByName(name)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + name)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        User saved = userRepository.save(user);
        return mapToSummaryDto(saved);
    }

    @Transactional
    public void toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        
        checkIdorAccess(user);
        
        boolean isTargetSuperAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_SUPER_ADMIN"));
        if (isTargetSuperAdmin && !isCurrentUserSuperAdmin()) {
            throw new AccessDeniedException("ADMIN cannot disable a SUPER_ADMIN user");
        }

        user.setEnabled(user.getEnabled() == null || !user.getEnabled());
        userRepository.save(user);
    }

    private void checkPrivilegeEscalation(List<String> targetRoles) {
        if (targetRoles == null) return;
        boolean wantsSuperAdmin = targetRoles.contains("ROLE_SUPER_ADMIN");
        if (wantsSuperAdmin && !isCurrentUserSuperAdmin()) {
            throw new AccessDeniedException("Only a SUPER_ADMIN can assign the ROLE_SUPER_ADMIN role");
        }
    }

    private void checkIdorAccess(User targetUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new AccessDeniedException("Not authenticated");

        String currentEmail = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPER_ADMIN"));

        if (!isAdmin && !currentEmail.equals(targetUser.getEmail())) {
            throw new AccessDeniedException("You do not have permission to access this user record");
        }
    }

    private boolean isCurrentUserSuperAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }

    private UserSummaryDto mapToSummaryDto(User user) {
        return UserSummaryDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhoneNumber())
                .enabled(user.getEnabled() != null ? user.getEnabled() : true)
                .roleNames(user.getRoles() != null
                        ? user.getRoles().stream().map(Role::getName).collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
}
