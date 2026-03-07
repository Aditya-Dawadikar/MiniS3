package com.example.minis3master.db.worker.repository;

import com.example.minis3master.db.worker.entity.BlobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlobRepository extends JpaRepository<BlobEntity, String> {
    Page<BlobEntity> findByLatestTrue(Pageable pageable);
}
