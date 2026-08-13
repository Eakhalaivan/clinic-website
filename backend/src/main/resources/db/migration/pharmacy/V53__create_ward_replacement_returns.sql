CREATE TABLE pharmacy_ward_replacement_returns (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    return_number VARCHAR(50) NOT NULL UNIQUE,
    request_number VARCHAR(50) NOT NULL,
    ward VARCHAR(100) NOT NULL,
    returned_by VARCHAR(150) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, COMPLETED, REJECTED
    return_date DATETIME(6) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE TABLE pharmacy_ward_replacement_return_items (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    return_id BIGINT NOT NULL,
    medicine_name VARCHAR(200) NOT NULL,
    returned_qty INT NOT NULL,
    CONSTRAINT fk_ward_repl_ret_item_return
        FOREIGN KEY (return_id) REFERENCES pharmacy_ward_replacement_returns(id)
);

CREATE INDEX idx_ward_repl_ret_status ON pharmacy_ward_replacement_returns(status);
