CREATE TABLE waitlist_entries (
    id BIGSERIAL PRIMARY KEY,
    doctor_id BIGINT NOT NULL REFERENCES doctor_profiles(id),
    patient_id BIGINT NOT NULL REFERENCES patient_profiles(id),
    desired_date_range_start TIMESTAMP WITH TIME ZONE,
    desired_date_range_end TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) DEFAULT 'WAITING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Update clinical_encounters
ALTER TABLE clinical_encounters ADD COLUMN opened_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE clinical_encounters ADD COLUMN closed_at TIMESTAMP WITH TIME ZONE;

-- Backfill opened_at and closed_at
UPDATE clinical_encounters SET opened_at = created_at;
UPDATE clinical_encounters SET closed_at = finalized_at WHERE finalized_at IS NOT NULL;

-- Update lab_test_requests to reference clinical_encounters instead of medical_records
-- We must drop the old constraint. The constraint name varies by database, but often we can just rename the column and add a new one, or find it dynamically.
-- Since this is PostgreSQL, we can use an alter table with drop constraint if we know the name, or just alter column type.
-- Wait, we can just drop the foreign key constraint if we know its name. Usually it's `lab_test_requests_encounter_id_fkey`.
ALTER TABLE lab_test_requests DROP CONSTRAINT IF EXISTS lab_test_requests_encounter_id_fkey;
ALTER TABLE lab_test_requests ADD CONSTRAINT lab_test_requests_encounter_id_fkey FOREIGN KEY (encounter_id) REFERENCES clinical_encounters(id) ON DELETE SET NULL;

ALTER TABLE lab_test_requests ADD COLUMN acknowledged_by BIGINT REFERENCES users(id);
ALTER TABLE lab_test_requests ADD COLUMN acknowledged_at TIMESTAMP WITH TIME ZONE;

-- Update prescriptions
ALTER TABLE prescriptions ADD COLUMN diagnosis_id BIGINT REFERENCES patient_diagnoses(id);
ALTER TABLE prescriptions ADD COLUMN override_reason TEXT;
