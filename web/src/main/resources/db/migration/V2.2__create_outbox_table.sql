CREATE TABLE IF NOT EXISTS public.outbox (
    id BIGSERIAL PRIMARY KEY,
    message_type VARCHAR(255) NOT NULL,
    payload text NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "user" VARCHAR(255) NOT NULL
);

CREATE INDEX idx_outbox_created_at ON outbox (created_at);