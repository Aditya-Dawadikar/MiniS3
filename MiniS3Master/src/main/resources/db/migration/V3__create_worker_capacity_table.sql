CREATE TABLE worker_capacity (
    worker_id TEXT PRIMARY KEY,
    total_disk_space BIGINT NOT NULL,
    updated_at BIGINT NOT NULL,
    FOREIGN KEY (worker_id) REFERENCES workers(id) ON DELETE CASCADE
);