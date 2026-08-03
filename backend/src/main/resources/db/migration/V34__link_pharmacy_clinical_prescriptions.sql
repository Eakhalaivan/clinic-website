-- V34: Link pharmacy_prescriptions to clinical prescriptions and add medication items

-- 1. Add foreign key back to the clinical prescriptions table
ALTER TABLE pharmacy_prescriptions
    ADD COLUMN IF NOT EXISTS clinical_prescription_id BIGINT REFERENCES prescriptions(id) ON DELETE SET NULL;

-- 2. Add medication items table for pharmacy prescriptions
CREATE TABLE IF NOT EXISTS pharmacy_prescription_items (
    id          BIGSERIAL PRIMARY KEY,
    pharmacy_prescription_id BIGINT NOT NULL REFERENCES pharmacy_prescriptions(id) ON DELETE CASCADE,
    medication_name VARCHAR(255) NOT NULL,
    dosage       VARCHAR(100),
    frequency    VARCHAR(100),
    duration     VARCHAR(100),
    instructions TEXT,
    created_at   TIMESTAMP DEFAULT NOW()
);

-- 3. Add dispensed_at / dispensed_by for sync-back to clinical side
ALTER TABLE pharmacy_prescriptions
    ADD COLUMN IF NOT EXISTS dispensed_at  TIMESTAMP,
    ADD COLUMN IF NOT EXISTS dispensed_by  VARCHAR(255);

-- 4. Add dispensed info to clinical prescriptions for patient-facing view
ALTER TABLE prescriptions
    ADD COLUMN IF NOT EXISTS pharmacy_status   VARCHAR(50)  DEFAULT 'PENDING',
    ADD COLUMN IF NOT EXISTS dispensed_at      TIMESTAMP,
    ADD COLUMN IF NOT EXISTS dispensed_by      VARCHAR(255);
