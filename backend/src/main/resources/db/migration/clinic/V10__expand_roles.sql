-- 1. Add login_portal to roles
ALTER TABLE roles ADD COLUMN login_portal VARCHAR(50);

-- Update existing roles
UPDATE roles SET login_portal = 'patient' WHERE name = 'ROLE_PATIENT';
UPDATE roles SET login_portal = 'doctor' WHERE name = 'ROLE_DOCTOR';
UPDATE roles SET login_portal = 'admin' WHERE name = 'ROLE_ADMIN';
UPDATE roles SET login_portal = 'branch-admin' WHERE name = 'ROLE_BRANCH_ADMIN';

-- Insert new roles
INSERT INTO roles (name, description, login_portal) VALUES 
('ROLE_SUPER_ADMIN', 'Super Administrator', 'super-admin'),
('ROLE_NURSE', 'Nurse', 'nurse'),
('ROLE_RECEPTION', 'Receptionist', 'reception'),
('ROLE_PHARMACIST', 'Pharmacist', 'pharmacist'),
('ROLE_LAB_TECH', 'Laboratory Technician', 'lab'),
('ROLE_RADIOLOGIST', 'Radiologist', 'radiologist'),
('ROLE_ACCOUNTANT', 'Accountant', 'accountant'),
('ROLE_HR', 'Human Resources', 'hr'),
('ROLE_FINANCE', 'Finance', 'finance'),
('ROLE_INVENTORY_MANAGER', 'Inventory Manager', 'inventory'),
('ROLE_CUSTOMER_SUPPORT', 'Customer Support', 'customer-support'),
('ROLE_MARKETING', 'Marketing', 'marketing'),
('ROLE_VENDOR', 'Vendor', 'vendor'),
('ROLE_INSURANCE', 'Insurance Provider', 'insurance'),
('ROLE_AMBULANCE', 'Ambulance Service', 'ambulance');

-- 2. Expand users table
ALTER TABLE users ADD COLUMN mfa_enabled BOOLEAN DEFAULT false;
ALTER TABLE users ADD COLUMN failed_login_attempts INT DEFAULT 0;
ALTER TABLE users ADD COLUMN locked_until TIMESTAMP WITH TIME ZONE;

-- 3. Create login_history table
CREATE TABLE login_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ip_address VARCHAR(45),
    user_agent TEXT,
    success BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. Create audit_log table
CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100),
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Create user_devices table
CREATE TABLE user_devices (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    device_id VARCHAR(255) NOT NULL,
    device_name VARCHAR(255),
    last_seen_at TIMESTAMP WITH TIME ZONE,
    trusted BOOLEAN DEFAULT false,
    UNIQUE(user_id, device_id)
);
