package com.example.minis3master.db.worker.repository;

import com.example.minis3master.db.worker.entity.WorkerRuntimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRuntimeRepository extends JpaRepository<WorkerRuntimeEntity, String> {
    List<WorkerRuntimeEntity> findByStatus(String status);
    
    @Query("SELECT w FROM WorkerRuntimeEntity w WHERE w.status = 'ACTIVE' ORDER BY w.availableDiskSpace DESC")
    List<WorkerRuntimeEntity> findActiveWorkersByAvailableSpace();
}
