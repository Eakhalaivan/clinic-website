-- V45: Pharmacy Clearance Schema

CREATE TABLE pharmacy_clearances (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    patient_name      VARCHAR(100) NOT NULL,
    uhid              VARCHAR(50) NOT NULL,
    ward              VARCHAR(50),
    admission_date    DATETIME,
    discharge_date    DATETIME,
    total_amount      DECIMAL(10, 2),
    total_due         DECIMAL(12, 2) NOT NULL DEFAULT 0,
    advance_adjusted  DECIMAL(12, 2) NOT NULL DEFAULT 0,
    net_payable       DECIMAL(12, 2) NOT NULL DEFAULT 0,
    status            VARCHAR(50) NOT NULL, -- PENDING, CLEARED
    cleared_at        DATETIME,
    cleared_by        VARCHAR(255),
    notes             TEXT,
    created_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pharmacy_clearances_patient ON pharmacy_clearances(patient_id);
CREATE INDEX idx_pharmacy_clearances_status ON pharmacy_clearances(status);
