package com.example.minis3master.db.worker.service;

import com.example.minis3master.db.worker.dto.WorkerFullDto;
import com.example.minis3master.db.worker.dto.FlatWorkerDto;
import com.example.minis3master.db.worker.entity.WorkerEntity;
import com.example.minis3master.db.worker.entity.WorkerCapacityEntity;
import com.example.minis3master.db.worker.entity.WorkerRuntimeEntity;
import com.example.minis3master.db.worker.repository.WorkerRepository;
import com.example.minis3master.db.worker.repository.WorkerCapacityRepository;
import com.example.minis3master.db.worker.repository.WorkerRuntimeRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerPersistenceService {
    private final WorkerRepository workerRepository;
    private final WorkerCapacityRepository workerCapacityRepository;
    private final WorkerRuntimeRepository workerRuntimeRepository;

    public WorkerPersistenceService(
            WorkerRepository workerRepository,
            WorkerCapacityRepository workerCapacityRepository,
            WorkerRuntimeRepository workerRuntimeRepository) {
        this.workerRepository = workerRepository;
        this.workerCapacityRepository = workerCapacityRepository;
        this.workerRuntimeRepository = workerRuntimeRepository;
    }

    // Worker CRUD methods
    public List<WorkerEntity> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Optional<WorkerEntity> getWorkerById(String id) {
        return workerRepository.findById(id);
    }

    public WorkerEntity saveWorker(WorkerEntity worker) {
        return workerRepository.save(worker);
    }

    public void deleteWorker(String id) {
        workerRepository.deleteById(id);
    }

    // WorkerCapacity CRUD methods
    public List<WorkerCapacityEntity> getAllWorkerCapacities() {
        return workerCapacityRepository.findAll();
    }

    public Optional<WorkerCapacityEntity> getWorkerCapacityById(String workerId) {
        return workerCapacityRepository.findById(workerId);
    }

    public WorkerCapacityEntity saveWorkerCapacity(WorkerCapacityEntity workerCapacity) {
        return workerCapacityRepository.save(workerCapacity);
    }

    public void deleteWorkerCapacity(String workerId) {
        workerCapacityRepository.deleteById(workerId);
    }

    // WorkerRuntime CRUD methods
    public List<WorkerRuntimeEntity> getAllWorkerRuntimes() {
        return workerRuntimeRepository.findAll();
    }

    public Optional<WorkerRuntimeEntity> getWorkerRuntimeById(String workerId) {
        return workerRuntimeRepository.findById(workerId);
    }

    public WorkerRuntimeEntity saveWorkerRuntime(WorkerRuntimeEntity workerRuntime) {
        return workerRuntimeRepository.save(workerRuntime);
    }

    public void deleteWorkerRuntime(String workerId) {
        workerRuntimeRepository.deleteById(workerId);
    }

    public List<WorkerRuntimeEntity> getWorkerRuntimesByStatus(String status) {
        return workerRuntimeRepository.findByStatus(status);
    }

    public List<WorkerRuntimeEntity> getActiveWorkersByAvailableSpace() {
        return workerRuntimeRepository.findActiveWorkersByAvailableSpace();
    }

    // Joined worker data methods
    public Optional<WorkerFullDto> getWorkerFullById(String workerId) {
        Optional<WorkerEntity> worker = workerRepository.findById(workerId);
        if (worker.isEmpty()) {
            return Optional.empty();
        }

        Optional<WorkerCapacityEntity> capacity = workerCapacityRepository.findById(workerId);
        Optional<WorkerRuntimeEntity> runtime = workerRuntimeRepository.findById(workerId);

        WorkerFullDto fullDto = new WorkerFullDto(
                worker.get(),
                capacity.orElse(null),
                runtime.orElse(null)
        );

        return Optional.of(fullDto);
    }

    public List<WorkerFullDto> getAllWorkersFull() {
        List<WorkerEntity> workers = workerRepository.findAll();
        List<WorkerFullDto> result = new ArrayList<>();

        for (WorkerEntity worker : workers) {
            WorkerCapacityEntity capacity = workerCapacityRepository.findById(worker.getId()).orElse(null);
            WorkerRuntimeEntity runtime = workerRuntimeRepository.findById(worker.getId()).orElse(null);
            
            result.add(new WorkerFullDto(worker, capacity, runtime));
        }

        return result;
    }

    // Flattened worker data methods (deduplicates fields)
    public Optional<FlatWorkerDto> getWorkerFlatById(String workerId) {
        Optional<WorkerEntity> worker = workerRepository.findById(workerId);
        if (worker.isEmpty()) {
            return Optional.empty();
        }

        Optional<WorkerCapacityEntity> capacity = workerCapacityRepository.findById(workerId);
        Optional<WorkerRuntimeEntity> runtime = workerRuntimeRepository.findById(workerId);

        return Optional.of(flattenWorkerData(
                worker.get(),
                capacity.orElse(null),
                runtime.orElse(null)
        ));
    }

    public List<FlatWorkerDto> getAllWorkersFlat() {
        List<WorkerEntity> workers = workerRepository.findAll();
        List<FlatWorkerDto> result = new ArrayList<>();

        for (WorkerEntity worker : workers) {
            WorkerCapacityEntity capacity = workerCapacityRepository.findById(worker.getId()).orElse(null);
            WorkerRuntimeEntity runtime = workerRuntimeRepository.findById(worker.getId()).orElse(null);
            
            result.add(flattenWorkerData(worker, capacity, runtime));
        }

        return result;
    }

    private FlatWorkerDto flattenWorkerData(WorkerEntity worker, WorkerCapacityEntity capacity, WorkerRuntimeEntity runtime) {
        return new FlatWorkerDto(
                worker.getId(),
                worker.getUrl(),
                worker.getCreatedAt(),
                worker.getUpdatedAt(),
                capacity != null ? capacity.getTotalDiskSpace() : null,
                runtime != null ? runtime.getStatus() : null,
                runtime != null ? runtime.getLastHeartbeat() : null,
                runtime != null ? runtime.getAvailableDiskSpace() : null,
                runtime != null ? runtime.getActiveUploads() : null,
                runtime != null ? runtime.getActiveDownloads() : null
        );
    }
}
