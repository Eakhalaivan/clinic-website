package com.healthcare.clinic.pharmacy.entity;

import com.healthcare.clinic.inventory.entity.BaseEntity;
import com.healthcare.clinic.inventory.entity.Patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity(name = "PharmacyRole")
@Table(name = "pharmacy_roles")
@SQLDelete(sql = "UPDATE pharmacy_roles SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
public class PharmacyRole extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(name = "permissions_json", columnDefinition = "TEXT")
    private String permissionsJson;

    @Column(name = "is_system_default")
    private Boolean isSystemDefault = false;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getPermissionsJson() { return permissionsJson; }
    public void setPermissionsJson(String permissionsJson) { this.permissionsJson = permissionsJson; }

    public Boolean getIsSystemDefault() { return isSystemDefault; }
    public void setIsSystemDefault(Boolean systemDefault) { isSystemDefault = systemDefault; }
}
