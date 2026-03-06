CREATE TABLE workers (
    id TEXT PRIMARY KEY,
    url TEXT NOT NULL UNIQUE,
    created_at BIGINT NOT NULL,
    updated_at BIGINT NOT NULL
);