ALTER TABLE prescriptions
ADD COLUMN voided_at TIMESTAMP,
ADD COLUMN void_reason VARCHAR(255);

ALTER TABLE patient_profiles
ADD COLUMN past_surgeries JSONB DEFAULT '[]',
ADD COLUMN family_history JSONB DEFAULT '[]',
ADD COLUMN current_medications JSONB DEFAULT '[]';
