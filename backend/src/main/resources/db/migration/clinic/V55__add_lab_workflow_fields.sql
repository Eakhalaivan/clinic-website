-- Phase 1: Explicit "Accept" step
ALTER TABLE lab_test_requests
ADD COLUMN accepted_at TIMESTAMP,
ADD COLUMN accepted_by_id BIGINT,
ADD CONSTRAINT fk_lab_requests_accepted_by FOREIGN KEY (accepted_by_id) REFERENCES users(id);

-- Phase 2: Barcode/QR sample tracking
ALTER TABLE lab_test_requests
ADD COLUMN sample_barcode_id VARCHAR(50) UNIQUE;

-- Phase 3: Real report upload
ALTER TABLE lab_results
ADD COLUMN report_file_url VARCHAR(255);
