package com.example.minis3master.db.worker.service;

import com.example.minis3master.db.worker.entity.BlobEntity;
import com.example.minis3master.db.worker.repository.BlobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlobService {
    private final BlobRepository blobRepository;

    public BlobService(BlobRepository blobRepository) {
        this.blobRepository = blobRepository;
    }

    public Page<BlobEntity> getAllBlobs(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);
        Pageable pageable = PageRequest.of(safePage, safeSize);
        return blobRepository.findByLatestTrue(pageable);
    }

    public Optional<BlobEntity> getBlobById(String fileId) {
        return blobRepository.findById(fileId);
    }

    public BlobEntity saveBlob(BlobEntity blobEntity) {
        return blobRepository.save(blobEntity);
    }

    public void deleteBlob(String fileId) {
        Optional<BlobEntity> existing = blobRepository.findById(fileId);
        if (existing.isEmpty()) {
            return;
        }

        BlobEntity blob = existing.get();
        blob.setIsDeleted(true);
        blob.setUpdatedAt(System.currentTimeMillis());
        blobRepository.save(blob);
    }
}
