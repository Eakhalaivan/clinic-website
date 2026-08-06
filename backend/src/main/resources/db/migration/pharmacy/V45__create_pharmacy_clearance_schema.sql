-- V45: Pharmacy Clearance Schema

CREATE TABLE pharmacy_clearances (
    id                BIGSERIAL PRIMARY KEY,
    patient_id        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    patient_name      VARCHAR(100) NOT NULL,
    uhid              VARCHAR(50) NOT NULL,
    ward              VARCHAR(50),
    admission_date    TIMESTAMP WITH TIME ZONE,
    total_due         DECIMAL(12, 2) NOT NULL DEFAULT 0,
    advance_adjusted  DECIMAL(12, 2) NOT NULL DEFAULT 0,
    net_payable       DECIMAL(12, 2) NOT NULL DEFAULT 0,
    status            VARCHAR(20) NOT NULL DEFAULT 'Pending', -- 'Pending' or 'Cleared'
    cleared_at        TIMESTAMP WITH TIME ZONE,
    cleared_by        BIGINT ,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pharmacy_clearances_patient ON pharmacy_clearances(patient_id);
CREATE INDEX idx_pharmacy_clearances_status ON pharmacy_clearances(status);
