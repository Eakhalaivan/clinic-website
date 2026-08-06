-- V28: Add missing indexes for Foreign Keys and frequent query filters
-- PostgreSQL does not automatically index foreign key columns or composite filter targets.

-- Identity & Core User indexes
CREATE INDEX IF NOT EXISTS idx_users_branch_id ON users(branch_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_otp_codes_user_id ON otp_codes(user_id);

-- Patient & Doctor Profile indexes
CREATE INDEX IF NOT EXISTS idx_patient_profiles_branch_id ON patient_profiles(branch_id);
CREATE INDEX IF NOT EXISTS idx_doctor_profiles_branch_id ON doctor_profiles(branch_id);
CREATE INDEX IF NOT EXISTS idx_doctor_profiles_is_active ON doctor_profiles(is_active);

-- Appointment & Slot indexes
CREATE INDEX IF NOT EXISTS idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX IF NOT EXISTS idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX IF NOT EXISTS idx_appointments_status ON appointments(status);
CREATE INDEX IF NOT EXISTS idx_appointment_slots_doctor_start ON appointment_slots(doctor_id, start_time);

-- Prescriptions & Medical Records indexes
CREATE INDEX IF NOT EXISTS idx_prescriptions_patient_id ON prescriptions(patient_id);
CREATE INDEX IF NOT EXISTS idx_prescriptions_doctor_id ON prescriptions(doctor_id);
CREATE INDEX IF NOT EXISTS idx_prescription_items_prescription_id ON prescription_items(prescription_id);

-- Lab & Diagnostic Request indexes
CREATE INDEX IF NOT EXISTS idx_lab_test_requests_patient_id ON lab_test_requests(patient_id);
CREATE INDEX IF NOT EXISTS idx_lab_test_requests_status ON lab_test_requests(status);
CREATE INDEX IF NOT EXISTS idx_lab_results_request_id ON lab_results(request_id);

-- Billing & Invoicing indexes
CREATE INDEX IF NOT EXISTS idx_invoices_patient_id ON invoices(patient_id);
CREATE INDEX IF NOT EXISTS idx_invoices_status ON invoices(status);

