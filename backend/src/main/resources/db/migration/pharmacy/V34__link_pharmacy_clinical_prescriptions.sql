-- V34: Link pharmacy_prescriptions to clinical prescriptions and add medication items
ALTER TABLE pharmacy_prescriptions
    ADD COLUMN IF NOT EXISTS clinical_prescription_id BIGINT;

CREATE TABLE IF NOT EXISTS pharmacy_prescription_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    pharmacy_prescription_id BIGINT NOT NULL REFERENCES pharmacy_prescriptions(id) ON DELETE CASCADE,
    medication_name VARCHAR(255) NOT NULL,
    dosage       VARCHAR(100),
    frequency    VARCHAR(100),
    duration     VARCHAR(100),
    instructions TEXT,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE pharmacy_prescriptions
    ADD COLUMN IF NOT EXISTS dispensed_at  DATETIME,
    ADD COLUMN IF NOT EXISTS dispensed_by  VARCHAR(255);
