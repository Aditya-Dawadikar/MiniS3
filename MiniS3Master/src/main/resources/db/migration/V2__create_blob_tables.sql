-- Blob Entity Table
CREATE TABLE IF NOT EXISTS blob (
    file_id TEXT PRIMARY KEY,
    file_name TEXT,
    file_size TEXT,
    uplaoded_at BIGINT,
    updated_at BIGINT,
    version BIGINT,
    replica_count BIGINT NOT NULL DEFAULT 0,
    is_deleted INTEGER NOT NULL DEFAULT 0,
    is_permantently_deleted INTEGER NOT NULL DEFAULT 0,
    parent_id TEXT,
    latest INTEGER NOT NULL DEFAULT 1
);

-- Blob Replica Relation Table

CREATE TABLE IF NOT EXISTS blob_replica (
    worker_id TEXT NOT NULL,
    file_id TEXT NOT NULL,
    replicated_at BIGINT,
    is_deleted INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (worker_id, file_id),
    FOREIGN KEY (worker_id) REFERENCES workers(id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES blob(file_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_worker_id ON blob_replica(worker_id);
CREATE INDEX IF NOT EXISTS idx_file_id ON blob_replica(file_id);
CREATE INDEX IF NOT EXISTS idx_blob_parent_id ON blob(parent_id);
CREATE INDEX IF NOT EXISTS idx_blob_latest ON blob(latest);
