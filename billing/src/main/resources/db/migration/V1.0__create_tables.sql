CREATE TABLE IF NOT EXISTS public.billing(
    id SERIAL  PRIMARY KEY,
    "user" VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    date TIMESTAMPTZ NOT NULL,
    quantity INTEGER NOT NULL,
    cost NUMERIC(12,2) NOT NULL
);
