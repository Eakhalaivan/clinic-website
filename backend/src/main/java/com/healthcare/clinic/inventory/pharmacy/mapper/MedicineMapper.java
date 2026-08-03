package com.healthcare.clinic.inventory.pharmacy.mapper;

import com.healthcare.clinic.inventory.pharmacy.dto.MedicineDTO;
import com.healthcare.clinic.inventory.entity.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "supplier.name", target = "supplierName")
    @Mapping(source = "supplier.address", target = "supplierAddress")
    @Mapping(source = "supplier.gstin", target = "supplierGstin")
    @Mapping(source = "supplier.contact", target = "supplierContact")
    com.healthcare.clinic.inventory.pharmacy.dto.MedicineDTO toDto(com.healthcare.clinic.inventory.entity.Medicine medicine);
    com.healthcare.clinic.inventory.entity.Medicine toEntity(com.healthcare.clinic.inventory.pharmacy.dto.MedicineDTO dto);
}
