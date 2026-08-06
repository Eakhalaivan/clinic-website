import os
import shutil

CLINIC_DIR = "backend/src/main/resources/db/migration/clinic"
PHARMACY_DIR = "backend/src/main/resources/db/migration/pharmacy"

os.makedirs(PHARMACY_DIR, exist_ok=True)

# 1. V9 goes entirely to pharmacy
shutil.move(f"{CLINIC_DIR}/V9__init_inventory_schema.sql", f"{PHARMACY_DIR}/V9__init_inventory_schema.sql")

# 2. V13 goes to pharmacy, but constraints must be removed
with open(f"{CLINIC_DIR}/V13__init_pharmacy_extensions_schema.sql", "r") as f:
    v13 = f.read()
v13 = v13.replace("REFERENCES prescriptions(id) ON DELETE CASCADE", "")
v13 = v13.replace("REFERENCES users(id) ON DELETE CASCADE", "")
with open(f"{PHARMACY_DIR}/V13__init_pharmacy_extensions_schema.sql", "w") as f:
    f.write(v13)
os.remove(f"{CLINIC_DIR}/V13__init_pharmacy_extensions_schema.sql")

# 3. V28 splitting
with open(f"{CLINIC_DIR}/V28__add_missing_indexes.sql", "r") as f:
    v28 = f.read()
pharmacy_v28_lines = []
clinic_v28_lines = []
for line in v28.splitlines():
    if "pharmacy_batch_return_to_supplier" in line or "pharmacy_credit_bills" in line or "Pharmacy / Inventory indexes" in line:
        pharmacy_v28_lines.append(line)
    else:
        clinic_v28_lines.append(line)

with open(f"{CLINIC_DIR}/V28__add_missing_indexes.sql", "w") as f:
    f.write("\n".join(clinic_v28_lines) + "\n")
with open(f"{PHARMACY_DIR}/V28__add_missing_indexes.sql", "w") as f:
    f.write("\n".join(pharmacy_v28_lines) + "\n")

# 4. V34 splitting
with open(f"{CLINIC_DIR}/V34__link_pharmacy_clinical_prescriptions.sql", "r") as f:
    v34 = f.read()

clinic_v34 = """-- V34: Clinical prescriptions sync-back
ALTER TABLE prescriptions
    ADD COLUMN IF NOT EXISTS pharmacy_status   VARCHAR(50)  DEFAULT 'PENDING',
    ADD COLUMN IF NOT EXISTS dispensed_at      TIMESTAMP,
    ADD COLUMN IF NOT EXISTS dispensed_by      VARCHAR(255);
"""

pharmacy_v34 = """-- V34: Link pharmacy_prescriptions to clinical prescriptions and add medication items
ALTER TABLE pharmacy_prescriptions
    ADD COLUMN IF NOT EXISTS clinical_prescription_id BIGINT;

CREATE TABLE IF NOT EXISTS pharmacy_prescription_items (
    id          BIGSERIAL PRIMARY KEY,
    pharmacy_prescription_id BIGINT NOT NULL REFERENCES pharmacy_prescriptions(id) ON DELETE CASCADE,
    medication_name VARCHAR(255) NOT NULL,
    dosage       VARCHAR(100),
    frequency    VARCHAR(100),
    duration     VARCHAR(100),
    instructions TEXT,
    created_at   TIMESTAMP DEFAULT NOW()
);

ALTER TABLE pharmacy_prescriptions
    ADD COLUMN IF NOT EXISTS dispensed_at  TIMESTAMP,
    ADD COLUMN IF NOT EXISTS dispensed_by  VARCHAR(255);
"""
with open(f"{CLINIC_DIR}/V34__link_pharmacy_clinical_prescriptions.sql", "w") as f:
    f.write(clinic_v34)
with open(f"{PHARMACY_DIR}/V34__link_pharmacy_clinical_prescriptions.sql", "w") as f:
    f.write(pharmacy_v34)

# 5. V35 splitting
with open(f"{CLINIC_DIR}/V35__add_prescription_item_type.sql", "r") as f:
    v35 = f.read()
clinic_v35 = []
pharmacy_v35 = []
current_bucket = clinic_v35
for line in v35.splitlines():
    if "pharmacy_prescription_items" in line:
        current_bucket = pharmacy_v35
    elif "prescription_items" in line and "pharmacy_" not in line:
        current_bucket = clinic_v35
    current_bucket.append(line)

with open(f"{CLINIC_DIR}/V35__add_prescription_item_type.sql", "w") as f:
    f.write("\n".join(clinic_v35) + "\n")
with open(f"{PHARMACY_DIR}/V35__add_prescription_item_type.sql", "w") as f:
    f.write("\n".join(pharmacy_v35) + "\n")

# 6. V37 splitting
with open(f"{CLINIC_DIR}/V37__add_advanced_prescription_fields.sql", "r") as f:
    v37 = f.read()
clinic_v37 = []
pharmacy_v37 = []
for line in v37.splitlines():
    if "pharmacy_prescription_items" in line:
        pharmacy_v37.append(line)
    else:
        clinic_v37.append(line)

with open(f"{CLINIC_DIR}/V37__add_advanced_prescription_fields.sql", "w") as f:
    f.write("\n".join(clinic_v37) + "\n")
with open(f"{PHARMACY_DIR}/V37__add_advanced_prescription_fields.sql", "w") as f:
    f.write("\n".join(pharmacy_v37) + "\n")

# 7. V45 goes entirely to pharmacy but needs constraint removal
with open(f"{CLINIC_DIR}/V45__create_pharmacy_clearance_schema.sql", "r") as f:
    v45 = f.read()
v45 = v45.replace("REFERENCES patients(id) ON DELETE CASCADE", "")
v45 = v45.replace("REFERENCES users(id) ON DELETE SET NULL", "")
with open(f"{PHARMACY_DIR}/V45__create_pharmacy_clearance_schema.sql", "w") as f:
    f.write(v45)
os.remove(f"{CLINIC_DIR}/V45__create_pharmacy_clearance_schema.sql")

# 8. V50 splitting
with open(f"{CLINIC_DIR}/V50__add_assigned_pharmacy_user_to_prescriptions.sql", "r") as f:
    v50 = f.read()
clinic_v50 = []
pharmacy_v50 = []
for line in v50.splitlines():
    if "pharmacy_prescriptions" in line:
        # replace the constraint references users(id)
        if "REFERENCES users(id)" in line:
            line = line.replace("REFERENCES users(id) ON DELETE SET NULL", "")
            # We also have to remove the CONSTRAINT part because foreign keys across DB are invalid
            # "ADD CONSTRAINT fk_... FOREIGN KEY (x) REFERENCES users" -> we just don't add the constraint at all!
            continue
        pharmacy_v50.append(line)
    else:
        clinic_v50.append(line)

with open(f"{CLINIC_DIR}/V50__add_assigned_pharmacy_user_to_prescriptions.sql", "w") as f:
    f.write("\n".join(clinic_v50) + "\n")
with open(f"{PHARMACY_DIR}/V50__add_assigned_pharmacy_user_to_prescriptions.sql", "w") as f:
    f.write("\n".join(pharmacy_v50) + "\n")

