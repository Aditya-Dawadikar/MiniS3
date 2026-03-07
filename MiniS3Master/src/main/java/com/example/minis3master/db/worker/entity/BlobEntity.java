package com.example.minis3master.db.worker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blob")
public class BlobEntity {
    @Id
    @Column(name = "file_id")
    private String fileId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private String fileSize;

    @Column(name = "uplaoded_at")
    private Long uploadedAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "version")
    private Long version;

    @Column(name = "replica_count")
    private Long replicaCount;

    @Column(name = "is_deleted", columnDefinition = "INTEGER")
    private Boolean isDeleted;

    @Column(name = "is_permantently_deleted", columnDefinition = "INTEGER")
    private Boolean isPermantentlyDeleted;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "latest", columnDefinition = "INTEGER")
    private Boolean latest;

    public BlobEntity() {
    }

    public BlobEntity(String fileId, String fileName, String fileSize, Long uploadedAt, Long updatedAt, Long version,
            Long replicaCount, Boolean isDeleted, Boolean isPermantentlyDeleted, String parentId, Boolean latest) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
        this.updatedAt = updatedAt;
        this.version = version;
        this.replicaCount = replicaCount;
        this.isDeleted = isDeleted;
        this.isPermantentlyDeleted = isPermantentlyDeleted;
        this.parentId = parentId;
        this.latest = latest;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Long getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Long uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getReplicaCount() {
        return replicaCount;
    }

    public void setReplicaCount(Long replicaCount) {
        this.replicaCount = replicaCount;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsPermantentlyDeleted() {
        return isPermantentlyDeleted;
    }

    public void setIsPermantentlyDeleted(Boolean isPermantentlyDeleted) {
        this.isPermantentlyDeleted = isPermantentlyDeleted;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getLatest() {
        return latest;
    }

    public void setLatest(Boolean latest) {
        this.latest = latest;
    }

}
