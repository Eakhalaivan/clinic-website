CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT,
    branch_id BIGINT,
    actor_id BIGINT,
    actor_username VARCHAR(100),
    action VARCHAR(100) NOT NULL,
    resource_type VARCHAR(100) NOT NULL,
    resource_id VARCHAR(100),
    old_state TEXT,
    new_state TEXT,
    reason VARCHAR(255),
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    correlation_id VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_tenant ON audit_logs(tenant_id);
CREATE INDEX idx_audit_logs_actor ON audit_logs(actor_id);
CREATE INDEX idx_audit_logs_resource ON audit_logs(resource_type, resource_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
