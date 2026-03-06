package com.example.minis3master.db.worker.repository;

import com.example.minis3master.db.worker.entity.WorkerCapacityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerCapacityRepository extends JpaRepository<WorkerCapacityEntity, String> {
}
