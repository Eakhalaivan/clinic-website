ALTER TABLE operating_hours ADD COLUMN department_id BIGINT;
ALTER TABLE operating_hours ADD COLUMN service_id BIGINT;
ALTER TABLE operating_hours ADD COLUMN doctor_id BIGINT;
ALTER TABLE operating_hours ADD COLUMN is_closed BOOLEAN DEFAULT FALSE;

ALTER TABLE operating_hours ALTER COLUMN open_time DROP NOT NULL;
ALTER TABLE operating_hours ALTER COLUMN close_time DROP NOT NULL;
