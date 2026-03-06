package com.example.minis3master.db.worker.dto;

/**
 * Flattened DTO for worker data - combines worker, capacity, and runtime information
 * with deduplicated fields to avoid redundant data in API responses.
 */
public class FlatWorkerDto {
    private String id;
    private String url;
    private Long createdAt;
    private Long updatedAt;
    private Long totalDiskSpace;
    private String status;
    private Long lastHeartbeat;
    private Long availableDiskSpace;
    private Integer activeUploads;
    private Integer activeDownloads;

    public FlatWorkerDto() {
    }

    public FlatWorkerDto(String id, String url, Long createdAt, Long updatedAt, Long totalDiskSpace,
                         String status, Long lastHeartbeat, Long availableDiskSpace,
                         Integer activeUploads, Integer activeDownloads) {
        this.id = id;
        this.url = url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalDiskSpace = totalDiskSpace;
        this.status = status;
        this.lastHeartbeat = lastHeartbeat;
        this.availableDiskSpace = availableDiskSpace;
        this.activeUploads = activeUploads;
        this.activeDownloads = activeDownloads;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getTotalDiskSpace() {
        return totalDiskSpace;
    }

    public void setTotalDiskSpace(Long totalDiskSpace) {
        this.totalDiskSpace = totalDiskSpace;
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
}
