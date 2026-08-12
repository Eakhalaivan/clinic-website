-- V45: Pharmacy Clearance Schema

CREATE TABLE pharmacy_clearances (
    id                BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    patient_id        BIGINT NOT NULL,
    patient_name      VARCHAR(100) NOT NULL,
    uhid              VARCHAR(50) NOT NULL,
    ward              VARCHAR(50),
    admission_date    DATETIME(6),
    total_due         DECIMAL(12, 2) NOT NULL DEFAULT 0,
    advance_adjusted  DECIMAL(12, 2) NOT NULL DEFAULT 0,
    net_payable       DECIMAL(12, 2) NOT NULL DEFAULT 0,
    status            VARCHAR(20) NOT NULL DEFAULT 'Pending', -- 'Pending' or 'Cleared'
    cleared_at        DATETIME(6),
    cleared_by        BIGINT,
    created_at        DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE INDEX idx_pharmacy_clearances_patient ON pharmacy_clearances(patient_id);
CREATE INDEX idx_pharmacy_clearances_status ON pharmacy_clearances(status);
