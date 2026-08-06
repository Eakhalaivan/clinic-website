ALTER TABLE clinic_outbox_events 
ADD COLUMN retry_count INT DEFAULT 0,
ADD COLUMN last_error TEXT;
