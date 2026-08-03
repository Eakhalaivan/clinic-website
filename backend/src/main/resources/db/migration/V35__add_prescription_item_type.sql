ALTER TABLE prescription_items
ADD COLUMN type VARCHAR(50);

ALTER TABLE pharmacy_prescription_items
ADD COLUMN type VARCHAR(50);
