ALTER TABLE nursing_notes
ADD COLUMN encounter_id BIGINT,
ADD COLUMN note_type VARCHAR(50) DEFAULT 'PROGRESS',
ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN version INT DEFAULT 0;

CREATE TABLE nursing_care_plans (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient_profiles(id) ON DELETE CASCADE,
    encounter_id BIGINT,
    nurse_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    diagnosis VARCHAR(255) NOT NULL,
    goals TEXT NOT NULL,
    interventions TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, COMPLETED, DISCONTINUED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    version INT DEFAULT 0
);

CREATE TABLE fall_risk_assessments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient_profiles(id) ON DELETE CASCADE,
    encounter_id BIGINT,
    nurse_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    score INT NOT NULL,
    risk_level VARCHAR(20) NOT NULL, -- LOW, MODERATE, HIGH
    assessed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

CREATE TABLE pain_assessments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient_profiles(id) ON DELETE CASCADE,
    encounter_id BIGINT,
    nurse_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    pain_score INT NOT NULL, -- 0-10
    pain_location VARCHAR(100),
    pain_characteristics VARCHAR(255),
    interventions TEXT,
    reassessment_time TIMESTAMP WITH TIME ZONE,
    assessed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
