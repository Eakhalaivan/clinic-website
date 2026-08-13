ALTER TABLE prescriptions ADD COLUMN voided_at TIMESTAMP;
ALTER TABLE prescriptions ADD COLUMN void_reason VARCHAR(255);

ALTER TABLE patient_profiles ADD COLUMN past_surgeries JSONB DEFAULT '[]';
ALTER TABLE patient_profiles ADD COLUMN family_history JSONB DEFAULT '[]';
ALTER TABLE patient_profiles ADD COLUMN current_medications JSONB DEFAULT '[]';
