package com.healthcare.clinic.pharmacy.mapper;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import com.healthcare.clinic.pharmacy.dto.MedicineDTO;
import com.healthcare.clinic.pharmacy.entity.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicineMapper {
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "supplier.address", target = "supplierAddress")
    @Mapping(source = "supplier.gstin", target = "supplierGstin")
    @Mapping(source = "supplier.contact", target = "supplierContact")
    com.healthcare.clinic.pharmacy.dto.MedicineDTO toDto(com.healthcare.clinic.pharmacy.entity.Medicine medicine);
    com.healthcare.clinic.pharmacy.entity.Medicine toEntity(com.healthcare.clinic.pharmacy.dto.MedicineDTO dto);
}
