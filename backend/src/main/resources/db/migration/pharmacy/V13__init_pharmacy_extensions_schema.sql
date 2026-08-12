-- We assume `medicines` and `pharmacy_prescriptions` tables already exist from V9.
-- This migration extends the pharmacy module.

CREATE TABLE medicine_batches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_id BIGINT NOT NULL REFERENCES medicines(id) ON DELETE CASCADE,
    batch_number VARCHAR(100) NOT NULL,
    expiry_date DATE NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(medicine_id, batch_number)
);

CREATE TABLE prescriptions_dispensed (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prescription_id BIGINT NOT NULL ,
    pharmacist_id BIGINT NOT NULL ,
    dispensed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

CREATE TABLE prescription_dispensed_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dispensed_id BIGINT NOT NULL REFERENCES prescriptions_dispensed(id) ON DELETE CASCADE,
    medicine_id BIGINT NOT NULL REFERENCES medicines(id) ON DELETE CASCADE,
    batch_id BIGINT REFERENCES medicine_batches(id) ON DELETE SET NULL,
    quantity_dispensed INT NOT NULL,
    price_charged DECIMAL(10,2) NOT NULL
);
