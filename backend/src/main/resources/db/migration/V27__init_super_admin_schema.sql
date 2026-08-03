-- V27: Super Admin Console Schema

CREATE TABLE system_configurations (
    id          BIGSERIAL PRIMARY KEY,
    config_key  VARCHAR(200) NOT NULL UNIQUE,
    config_val  TEXT NOT NULL,
    description TEXT,
    updated_by  VARCHAR(100),
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed default platform configurations
INSERT INTO system_configurations (config_key, config_val, description, updated_by) VALUES
  ('clinic.maintenance_mode',    'false',     'Enable maintenance mode (blocks all logins)',          'system'),
  ('clinic.max_login_attempts',  '5',         'Maximum failed logins before account lockout',         'system'),
  ('clinic.session_timeout_min', '60',        'Session inactivity timeout in minutes',                'system'),
  ('clinic.appointment_slot_min','30',        'Default appointment slot duration in minutes',         'system'),
  ('clinic.email_notifications', 'true',      'Enable outbound email notifications globally',         'system'),
  ('clinic.sms_notifications',   'false',     'Enable outbound SMS notifications globally',           'system'),
  ('clinic.currency_symbol',     'INR',       'Platform currency symbol',                             'system'),
  ('clinic.timezone',            'Asia/Kolkata','Default server timezone',                            'system');

CREATE TABLE subscription_plans (
    id             BIGSERIAL PRIMARY KEY,
    plan_name      VARCHAR(100) NOT NULL UNIQUE,
    price_monthly  DECIMAL(12, 2) NOT NULL,
    price_annually DECIMAL(12, 2),
    max_users      INT NOT NULL DEFAULT 10,
    max_branches   INT NOT NULL DEFAULT 1,
    features       TEXT, -- JSON array of feature flag strings
    is_active      BOOLEAN NOT NULL DEFAULT true,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO subscription_plans (plan_name, price_monthly, price_annually, max_users, max_branches, features) VALUES
  ('Starter',     999.00,   9990.00,   5,    1, '["appointments","billing","pharmacy"]'),
  ('Growth',      2499.00,  24990.00,  25,   3, '["appointments","billing","pharmacy","lab","radiology","hr"]'),
  ('Enterprise',  7499.00,  74990.00,  200,  20,'["appointments","billing","pharmacy","lab","radiology","hr","finance","inventory","crm","ecommerce","ambulance","insurance"]');

CREATE TABLE audit_logs (
    id          BIGSERIAL PRIMARY KEY,
    actor_id    BIGINT REFERENCES users(id) ON DELETE SET NULL,
    actor_email VARCHAR(200),
    action      VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100),
    entity_id   VARCHAR(100),
    details     TEXT,
    ip_address  VARCHAR(45),
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at DESC);
CREATE INDEX idx_audit_logs_actor ON audit_logs(actor_id);
