ALTER TABLE queue_tokens ADD CONSTRAINT unique_branch_date_token UNIQUE (branch_id, generated_date, token_number);
