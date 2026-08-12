-- Add Advanced Prescription fields
ALTER TABLE prescriptions ADD COLUMN chief_complaint TEXT;
ALTER TABLE prescriptions ADD COLUMN diagnosis TEXT;
ALTER TABLE prescriptions ADD COLUMN symptoms TEXT;
ALTER TABLE prescriptions ADD COLUMN medical_history TEXT;
ALTER TABLE prescriptions ADD COLUMN follow_up_date TIMESTAMP;

-- Add Advanced PrescriptionItem fields
ALTER TABLE prescription_items ADD COLUMN strength VARCHAR(50);
ALTER TABLE prescription_items ADD COLUMN timing VARCHAR(50);

-- Also add to pharmacy mirroring table

-- Vitals table
CREATE TABLE vitals (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT,
    height_cm INT,
    weight_kg INT,
    blood_pressure VARCHAR(50),
    pulse_bpm INT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patient_profiles(id)
);
