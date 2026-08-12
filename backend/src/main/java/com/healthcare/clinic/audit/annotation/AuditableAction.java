package com.healthcare.clinic.audit.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditableAction {
    
    /**
     * Name of the module (e.g., "PATIENT", "PHARMACY", "LAB")
     */
    String module();
    
    /**
     * The action being performed (e.g., "VIEW", "EDIT", "DOWNLOAD", "DISPENSE", "REFUND")
     */
    String action();
    
    /**
     * Type of resource being acted upon (e.g., "PatientProfile", "LabResult", "Invoice")
     */
    String resourceType() default "";
    
    /**
     * Sensitivity level of the action (NORMAL, HIGH, PHI_RESTRICTED)
     */
    String sensitivityLevel() default "NORMAL";
}
