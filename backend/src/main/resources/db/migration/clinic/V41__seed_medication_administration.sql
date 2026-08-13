INSERT INTO medication_administration_records (patient_id, prescription_item_id, patient_name, bed_number, medication_name, dosage, scheduled_time, status, administered_by_user_id) VALUES
(1, 101, 'Sunita Sharma', '04', 'Paracetamol 500mg', '1 Tab', CURRENT_TIMESTAMP - INTERVAL '2' HOUR, 'GIVEN', 1),
(1, 102, 'Sunita Sharma', '04', 'Amoxicillin 500mg', '1 Cap', CURRENT_TIMESTAMP + INTERVAL '1' HOUR, 'DUE', NULL),
(2, 103, 'Vikram Singh', '12', 'Ceftriaxone 1g IV', '1 Vial', CURRENT_TIMESTAMP - INTERVAL '1' HOUR, 'GIVEN', 1),
(3, 104, 'Ananya Patel', '08', 'Pantoprazole 40mg IV', '1 Vial', CURRENT_TIMESTAMP - INTERVAL '3' HOUR, 'GIVEN', 1);
