-- V56: Reconcile Schema Drift
-- Drops orphaned pharmacy tables and creates tables that were missing from the live DB.

DROP TABLE IF EXISTS medicine_batches CASCADE;
DROP TABLE IF EXISTS prescription_dispensed_items CASCADE;
DROP TABLE IF EXISTS prescriptions_dispensed CASCADE;
