-- V30: Fix null defaults for user flags
UPDATE users SET mfa_enabled = false WHERE mfa_enabled IS NULL;
UPDATE users SET enabled = true WHERE enabled IS NULL;
UPDATE users SET failed_login_attempts = 0 WHERE failed_login_attempts IS NULL;
