#!/bin/bash
export PGPASSWORD=AdminPass123!
psql -h localhost -U eakhalaivan -d clinic_platform -c "INSERT INTO branches (id, name, address, phone_number, is_active) VALUES (1, 'Main Branch', '123 Health Ave', '+1234567890', true) ON CONFLICT DO NOTHING;"
