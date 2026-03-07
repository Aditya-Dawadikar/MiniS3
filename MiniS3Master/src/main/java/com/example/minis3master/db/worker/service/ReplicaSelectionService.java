package com.example.minis3master.db.worker.service;

import com.example.minis3master.db.worker.entity.WorkerEntity;
import com.example.minis3master.db.worker.entity.WorkerRuntimeEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReplicaSelectionService {
    private final WorkerPersistenceService workerPersistenceService;

    public ReplicaSelectionService(WorkerPersistenceService workerPersistenceService) {
        this.workerPersistenceService = workerPersistenceService;
    }

    public List<WorkerEntity> getUploadCandidatesByAvailableSpace(int limit) {
        List<WorkerEntity> workers = workerPersistenceService.getAllWorkers();
        List<WorkerRuntimeEntity> runtimes = workerPersistenceService.getAllWorkerRuntimes();

        Map<String, Long> availableSpaceByWorkerId = new HashMap<>();
        for (WorkerRuntimeEntity runtime : runtimes) {
            long available = runtime.getAvailableDiskSpace() != null ? runtime.getAvailableDiskSpace() : 0L;
            availableSpaceByWorkerId.put(runtime.getWorkerId(), available);
        }

        workers.sort(Comparator.comparingLong(
            (WorkerEntity worker) -> availableSpaceByWorkerId.getOrDefault(worker.getId(), 0L)
        ).reversed());

        if (limit <= 0 || workers.size() <= limit) {
            return workers;
        }
        return workers.subList(0, limit);
    }
}
