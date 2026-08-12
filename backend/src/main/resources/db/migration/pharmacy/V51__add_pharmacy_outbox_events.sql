CREATE TABLE pharmacy_outbox_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at DATETIME(6) DEFAULT CURRENT_DATETIME(6),
    processed_at DATETIME(6)
);
CREATE INDEX idx_pharmacy_outbox_status ON pharmacy_outbox_events(status);
