-- V34: Clinical prescriptions sync-back
ALTER TABLE prescriptions ADD COLUMN pharmacy_status VARCHAR(50) DEFAULT 'PENDING';
ALTER TABLE prescriptions ADD COLUMN dispensed_at TIMESTAMP;
ALTER TABLE prescriptions ADD COLUMN dispensed_by VARCHAR(255);
