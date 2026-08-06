-- Update any existing status values if necessary, though they should match the enum exactly.
-- Add check constraint for valid enum values

ALTER TABLE appointments 
ADD CONSTRAINT chk_appointments_status 
CHECK (status IN (
    'AVAILABLE',
    'BOOKED',
    'CONFIRMED',
    'CHECKED_IN',
    'WAITING',
    'IN_CONSULTATION',
    'COMPLETED',
    'FOLLOW_UP_SCHEDULED',
    'CANCELLED',
    'NO_SHOW'
));
