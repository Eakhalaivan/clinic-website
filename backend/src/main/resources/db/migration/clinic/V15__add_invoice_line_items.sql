-- Extend invoices table with line-item billing fields
ALTER TABLE invoices ADD COLUMN invoice_number VARCHAR(50) UNIQUE;
ALTER TABLE invoices ADD COLUMN tax_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoices ADD COLUMN discount_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoices ADD COLUMN total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoices ADD COLUMN payment_method VARCHAR(30);
ALTER TABLE invoices ADD COLUMN paid_at TIMESTAMP;
ALTER TABLE invoices ADD COLUMN branch_id BIGINT REFERENCES branches(id) ON DELETE SET NULL;

-- Generate invoice numbers for any existing rows
UPDATE invoices SET invoice_number = CONCAT('INV-', LPAD(id::TEXT, 5, '0')) WHERE invoice_number IS NULL;

-- Create invoice line items table
CREATE TABLE invoice_items (
    id BIGSERIAL PRIMARY KEY,
    invoice_id BIGINT NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    description VARCHAR(500) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    item_type VARCHAR(50) NOT NULL DEFAULT 'OTHER', -- APPOINTMENT, LAB_TEST, PHARMACY, CONSULTATION, OTHER
    reference_id BIGINT -- nullable FK to the originating record
);
