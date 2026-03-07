package com.example.minis3master.db.worker.repository;

import com.example.minis3master.db.worker.entity.BlobReplicationEntity;
import com.example.minis3master.db.worker.entity.BlobReplicationEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlobReplicationRepository extends JpaRepository<BlobReplicationEntity, BlobReplicationEntityPK> {
    List<BlobReplicationEntity> findByWorkerId(String workerId);
    List<BlobReplicationEntity> findByFileId(String fileId);
    long countByFileIdAndIsDeletedFalse(String fileId);
}
