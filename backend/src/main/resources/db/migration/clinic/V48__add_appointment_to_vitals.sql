ALTER TABLE vital_signs
ADD COLUMN appointment_id BIGINT;

ALTER TABLE vital_signs
ADD CONSTRAINT fk_vital_signs_appointment
FOREIGN KEY (appointment_id)
REFERENCES appointments(id);
