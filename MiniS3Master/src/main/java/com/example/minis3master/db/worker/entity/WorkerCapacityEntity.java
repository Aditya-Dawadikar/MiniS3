package com.example.minis3master.db.worker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "worker_capacity")
public class WorkerCapacityEntity {
    @Id
    @Column(name = "worker_id")
    private String workerId;

    @Column(name = "total_disk_space", nullable = false)
    private Long totalDiskSpace;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    public WorkerCapacityEntity() {
    }

    public WorkerCapacityEntity(String workerId, Long totalDiskSpace, Long updatedAt) {
        this.workerId = workerId;
        this.totalDiskSpace = totalDiskSpace;
        this.updatedAt = updatedAt;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public Long getTotalDiskSpace() {
        return totalDiskSpace;
    }

    public void setTotalDiskSpace(Long totalDiskSpace) {
        this.totalDiskSpace = totalDiskSpace;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
