CREATE TABLE doctor_prescription_templates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    chief_complaint TEXT,
    diagnosis TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_doc_template (doctor_id)
);

CREATE TABLE doctor_prescription_template_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_id BIGINT NOT NULL,
    medication_name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    strength VARCHAR(50),
    dosage VARCHAR(50),
    frequency VARCHAR(100),
    duration VARCHAR(50),
    timing VARCHAR(50),
    instructions TEXT,
    FOREIGN KEY (template_id) REFERENCES doctor_prescription_templates(id) ON DELETE CASCADE
);
