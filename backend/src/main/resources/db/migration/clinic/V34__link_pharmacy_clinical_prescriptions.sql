-- V34: Clinical prescriptions sync-back
ALTER TABLE prescriptions
    ADD COLUMN IF NOT EXISTS pharmacy_status   VARCHAR(50)  DEFAULT 'PENDING',
    ADD COLUMN IF NOT EXISTS dispensed_at      TIMESTAMP,
    ADD COLUMN IF NOT EXISTS dispensed_by      VARCHAR(255);
