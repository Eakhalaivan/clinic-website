ALTER TABLE appointments ADD COLUMN tenant_id BIGINT;
-- branch_id already exists in appointments

ALTER TABLE patient_profiles ADD COLUMN tenant_id BIGINT;
-- branch_id already exists in patient_profiles

ALTER TABLE invoices ADD COLUMN tenant_id BIGINT;
-- branch_id already exists in invoices

ALTER TABLE emergency_patient_records ADD COLUMN tenant_id BIGINT;
ALTER TABLE emergency_patient_records ADD COLUMN branch_id BIGINT;

-- Add foreign keys for tenant and branch
ALTER TABLE appointments ADD CONSTRAINT fk_appointments_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id);
ALTER TABLE appointments ADD CONSTRAINT fk_appointments_branch FOREIGN KEY (branch_id) REFERENCES branches(id);

ALTER TABLE patient_profiles ADD CONSTRAINT fk_patient_profiles_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id);
ALTER TABLE patient_profiles ADD CONSTRAINT fk_patient_profiles_branch FOREIGN KEY (branch_id) REFERENCES branches(id);

ALTER TABLE invoices ADD CONSTRAINT fk_invoices_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id);
-- fk for invoices to branch already exists from V15

ALTER TABLE emergency_patient_records ADD CONSTRAINT fk_emergency_records_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id);
ALTER TABLE emergency_patient_records ADD CONSTRAINT fk_emergency_records_branch FOREIGN KEY (branch_id) REFERENCES branches(id);
