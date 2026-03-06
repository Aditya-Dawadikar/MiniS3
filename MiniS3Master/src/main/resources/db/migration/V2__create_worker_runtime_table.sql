CREATE TABLE worker_runtime (
    worker_id TEXT PRIMARY KEY,
    status TEXT NOT NULL,
    last_heartbeat BIGINT NOT NULL,
    available_disk_space BIGINT NOT NULL,
    active_uploads INTEGER NOT NULL DEFAULT 0,
    active_downloads INTEGER NOT NULL DEFAULT 0,
    updated_at BIGINT NOT NULL,
    FOREIGN KEY (worker_id) REFERENCES workers(id) ON DELETE CASCADE
);

CREATE INDEX idx_worker_runtime_status
ON worker_runtime(status);

CREATE INDEX idx_worker_runtime_available_disk_space
ON worker_runtime(available_disk_space);

CREATE INDEX idx_worker_runtime_last_heartbeat
ON worker_runtime(last_heartbeat);