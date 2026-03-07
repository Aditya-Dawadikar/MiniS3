package com.example.minis3master.db.worker.service;

import com.example.minis3master.db.worker.entity.BlobReplicationEntity;
import com.example.minis3master.db.worker.entity.BlobReplicationEntityPK;
import com.example.minis3master.db.worker.repository.BlobReplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlobReplicationService {
    private final BlobReplicationRepository blobReplicationRepository;

    public BlobReplicationService(BlobReplicationRepository blobReplicationRepository) {
        this.blobReplicationRepository = blobReplicationRepository;
    }

    public List<BlobReplicationEntity> getAllBlobReplications() {
        return blobReplicationRepository.findAll();
    }

    public Optional<BlobReplicationEntity> getBlobReplicationById(String workerId, String fileId) {
        BlobReplicationEntityPK pk = new BlobReplicationEntityPK(workerId, fileId);
        return blobReplicationRepository.findById(pk);
    }

    public BlobReplicationEntity saveBlobReplication(BlobReplicationEntity blobReplicationEntity) {
        return blobReplicationRepository.save(blobReplicationEntity);
    }

    public void deleteBlobReplication(String workerId, String fileId) {
        BlobReplicationEntityPK pk = new BlobReplicationEntityPK(workerId, fileId);
        blobReplicationRepository.deleteById(pk);
    }

    public List<BlobReplicationEntity> getBlobReplicationsByWorkerId(String workerId) {
        return blobReplicationRepository.findByWorkerId(workerId);
    }

    public List<BlobReplicationEntity> getBlobReplicationsByFileId(String fileId) {
        return blobReplicationRepository.findByFileId(fileId);
    }

    public long getActiveReplicaCountByFileId(String fileId) {
        return blobReplicationRepository.countByFileIdAndIsDeletedFalse(fileId);
    }
}
