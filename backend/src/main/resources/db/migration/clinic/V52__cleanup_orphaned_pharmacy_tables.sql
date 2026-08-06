-- Phase 7 Cleanup: Drop pharmacy tables from the clinic database
-- These tables are now hosted in the separate pharmacy_db (MySQL/TiDB)
-- ONLY run this after data has been verified in the new database.

DROP TABLE IF EXISTS medicine_stock CASCADE;
DROP TABLE IF EXISTS medicine CASCADE;
DROP TABLE IF EXISTS supplier CASCADE;
DROP TABLE IF EXISTS goods_receipt_note_item CASCADE;
DROP TABLE IF EXISTS goods_receipt_note CASCADE;
DROP TABLE IF EXISTS pharmacy_patient CASCADE;
DROP TABLE IF EXISTS pharmacy_role CASCADE;
DROP TABLE IF EXISTS activity_log CASCADE;
DROP TABLE IF EXISTS pharmacy_prescription_items CASCADE;
DROP TABLE IF EXISTS pharmacy_prescription_records CASCADE;
DROP TABLE IF EXISTS prescription_dispensed_items CASCADE;
DROP TABLE IF EXISTS prescription_dispensed CASCADE;
DROP TABLE IF EXISTS pharmacy_bills CASCADE;
