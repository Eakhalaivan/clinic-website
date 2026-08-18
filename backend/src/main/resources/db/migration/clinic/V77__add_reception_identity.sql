-- V77__add_reception_identity.sql

-- Add OP number and duplicate tracking to patient_profiles
ALTER TABLE patient_profiles ADD COLUMN op_number VARCHAR(50) UNIQUE;
ALTER TABLE patient_profiles ADD COLUMN is_duplicate_of BIGINT REFERENCES patient_profiles(id);
ALTER TABLE patient_profiles ADD COLUMN merge_reason VARCHAR(255);
ALTER TABLE patient_profiles ADD COLUMN preferred_communication VARCHAR(20) DEFAULT 'EMAIL';

-- Patient Identity Verification table
CREATE TABLE patient_identity_verifications (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient_profiles(id),
    verification_method VARCHAR(50) NOT NULL, -- e.g., OTP, GOVERNMENT_ID, STAFF_CONFIRMATION
    verified_by_user_id BIGINT NOT NULL REFERENCES users(id),
    verified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL, -- SUCCESS, FAILED, PENDING
    failure_reason VARCHAR(255),
    document_reference VARCHAR(255)
);

CREATE INDEX idx_patient_op_number ON patient_profiles(op_number);
CREATE INDEX idx_patient_identity_patient ON patient_identity_verifications(patient_id);
