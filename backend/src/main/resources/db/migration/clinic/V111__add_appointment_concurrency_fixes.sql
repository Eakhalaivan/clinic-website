ALTER TABLE appointments ADD COLUMN idempotency_key VARCHAR(100) UNIQUE;

DO $$ 
DECLARE 
    constraint_name text;
BEGIN
    SELECT tc.constraint_name INTO constraint_name
    FROM information_schema.table_constraints tc
    JOIN information_schema.key_column_usage kcu
      ON tc.constraint_name = kcu.constraint_name
    WHERE tc.table_name = 'appointments' 
      AND kcu.column_name = 'slot_id' 
      AND tc.constraint_type = 'UNIQUE';

    IF constraint_name IS NOT NULL THEN
        EXECUTE 'ALTER TABLE appointments DROP CONSTRAINT ' || constraint_name;
    END IF;
END $$;

CREATE UNIQUE INDEX idx_unique_active_slot ON appointments(slot_id) WHERE status != 'CANCELLED';
