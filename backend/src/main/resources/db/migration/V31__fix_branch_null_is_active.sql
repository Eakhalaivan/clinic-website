UPDATE branches SET is_active = true WHERE is_active IS NULL;
ALTER TABLE branches ALTER COLUMN is_active SET DEFAULT true;
