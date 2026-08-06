package com.healthcare.clinic.inventory.mapper;

import com.healthcare.clinic.inventory.dto.CreateUserRequest;
import com.healthcare.clinic.inventory.dto.UserResponseDTO;
import com.healthcare.clinic.pharmacy.entity.PharmacyUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PharmacyUser toEntity(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CreateUserRequest request, @MappingTarget PharmacyUser user);

    UserResponseDTO toResponseDto(PharmacyUser user);

    default String mapRoleToString(com.healthcare.clinic.pharmacy.entity.PharmacyRole role) {
        return role != null ? role.getName() : null;
    }
}
