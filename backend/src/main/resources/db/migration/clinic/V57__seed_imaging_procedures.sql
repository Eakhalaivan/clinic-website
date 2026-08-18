-- V57: Seed Imaging Procedures

INSERT INTO imaging_procedures (code, name, modality, body_part, price, is_active)
VALUES
  ('RAD-XRAY-001', 'Chest X-Ray (PA View)', 'XRAY', 'Chest', 50.00, true),
  ('RAD-MRI-001', 'MRI Brain (Without Contrast)', 'MRI', 'Brain', 500.00, true),
  ('RAD-CT-001', 'CT Scan Abdomen', 'CT', 'Abdomen', 300.00, true),
  ('RAD-US-001', 'Ultrasound Pelvis', 'ULTRASOUND', 'Pelvis', 150.00, true),
  ('RAD-PET-001', 'PET Scan Whole Body', 'PET', 'Whole Body', 1200.00, true);
