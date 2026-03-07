package com.example.minis3master.db.worker.entity;

import java.io.Serializable;
import java.util.Objects;

public class BlobReplicationEntityPK implements Serializable {
    private String workerId;
    private String fileId;

    public BlobReplicationEntityPK() {
    }

    public BlobReplicationEntityPK(String workerId, String fileId) {
        this.workerId = workerId;
        this.fileId = fileId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlobReplicationEntityPK that = (BlobReplicationEntityPK) o;
        return Objects.equals(workerId, that.workerId) &&
               Objects.equals(fileId, that.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workerId, fileId);
    }
}
