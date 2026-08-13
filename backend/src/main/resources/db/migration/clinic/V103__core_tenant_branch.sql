ALTER TABLE appointments ADD COLUMN tenant_id BIGINT;
ALTER TABLE appointments ADD COLUMN branch_id BIGINT;

ALTER TABLE patients ADD COLUMN tenant_id BIGINT;
ALTER TABLE patients ADD COLUMN branch_id BIGINT;

ALTER TABLE invoices ADD COLUMN tenant_id BIGINT;
ALTER TABLE invoices ADD COLUMN branch_id BIGINT;

ALTER TABLE emergency_patient_records ADD COLUMN tenant_id BIGINT;
ALTER TABLE emergency_patient_records ADD COLUMN branch_id BIGINT;

-- Add foreign keys for tenant and branch
ALTER TABLE appointments ADD CONSTRAINT fk_appointments_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id);
ALTER TABLE appointments ADD CONSTRAINT fk_appointments_branch FOREIGN KEY (branch_id) REFERENCES branches(id);

ALTER TABLE patients ADD CONSTRAINT fk_patients_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id);
ALTER TABLE patients ADD CONSTRAINT fk_patients_branch FOREIGN KEY (branch_id) REFERENCES branches(id);

ALTER TABLE invoices ADD CONSTRAINT fk_invoices_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id);
ALTER TABLE invoices ADD CONSTRAINT fk_invoices_branch FOREIGN KEY (branch_id) REFERENCES branches(id);

ALTER TABLE emergency_patient_records ADD CONSTRAINT fk_emergency_records_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id);
ALTER TABLE emergency_patient_records ADD CONSTRAINT fk_emergency_records_branch FOREIGN KEY (branch_id) REFERENCES branches(id);
