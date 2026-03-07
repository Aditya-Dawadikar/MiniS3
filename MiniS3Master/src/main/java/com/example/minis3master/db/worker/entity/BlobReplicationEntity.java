package com.example.minis3master.db.worker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.Table;


@Entity
@Table(name="blob_replica", indexes={
    @Index(name="idx_worker_id", columnList="worker_id"),
    @Index(name="idx_file_id", columnList="file_id")
})

@IdClass(BlobReplicationEntityPK.class)
public class BlobReplicationEntity {
    
    @Id
    @Column(name="worker_id")
    private String workerId;

    @Id
    @Column(name="file_id")
    private String fileId;

    @Column(name="replicated_at")
    private Long replicatedAt;

    @Column(name="is_deleted", columnDefinition = "INTEGER")
    private Boolean isDeleted;

    public BlobReplicationEntity() {
    }

    public BlobReplicationEntity(String workerId, String fileId, Long replicatedAt, Boolean isDeleted) {
        this.workerId = workerId;
        this.fileId = fileId;
        this.replicatedAt = replicatedAt;
        this.isDeleted = isDeleted;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Long getReplicatedAt() {
        return replicatedAt;
    }

    public void setReplicatedAt(Long replicatedAt) {
        this.replicatedAt = replicatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
