package com.example.minis3master.db.worker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "worker_runtime")
public class WorkerRuntimeEntity {

    @Id
    @Column(name = "worker_id")
    private String workerId;

    @Column(nullable = false)
    private String status;

    @Column(name = "last_heartbeat", nullable = false)
    private Long lastHeartbeat;

    @Column(name = "available_disk_space", nullable = false)
    private Long availableDiskSpace;

    @Column(name = "active_uploads", nullable = false)
    private Integer activeUploads;

    @Column(name = "active_downloads", nullable = false)
    private Integer activeDownloads;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

    public WorkerRuntimeEntity() {
    }

    public WorkerRuntimeEntity(String workerId, String status, Long lastHeartbeat,
                               Long availableDiskSpace, Integer activeUploads,
                               Integer activeDownloads, Long updatedAt) {
        this.workerId = workerId;
        this.status = status;
        this.lastHeartbeat = lastHeartbeat;
        this.availableDiskSpace = availableDiskSpace;
        this.activeUploads = activeUploads;
        this.activeDownloads = activeDownloads;
        this.updatedAt = updatedAt;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public Long getAvailableDiskSpace() {
        return availableDiskSpace;
    }

    public void setAvailableDiskSpace(Long availableDiskSpace) {
        this.availableDiskSpace = availableDiskSpace;
    }

    public Integer getActiveUploads() {
        return activeUploads;
    }

    public void setActiveUploads(Integer activeUploads) {
        this.activeUploads = activeUploads;
    }

    public Integer getActiveDownloads() {
        return activeDownloads;
    }

    public void setActiveDownloads(Integer activeDownloads) {
        this.activeDownloads = activeDownloads;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
