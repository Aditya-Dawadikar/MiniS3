package com.example.minis3master.db.worker.dto;

import com.example.minis3master.db.worker.entity.WorkerEntity;
import com.example.minis3master.db.worker.entity.WorkerCapacityEntity;
import com.example.minis3master.db.worker.entity.WorkerRuntimeEntity;

public class WorkerFullDto {
    private WorkerEntity worker;
    private WorkerCapacityEntity capacity;
    private WorkerRuntimeEntity runtime;

    public WorkerFullDto() {
    }

    public WorkerFullDto(WorkerEntity worker, WorkerCapacityEntity capacity, WorkerRuntimeEntity runtime) {
        this.worker = worker;
        this.capacity = capacity;
        this.runtime = runtime;
    }

    public WorkerEntity getWorker() {
        return worker;
    }

    public void setWorker(WorkerEntity worker) {
        this.worker = worker;
    }

    public WorkerCapacityEntity getCapacity() {
        return capacity;
    }

    public void setCapacity(WorkerCapacityEntity capacity) {
        this.capacity = capacity;
    }

    public WorkerRuntimeEntity getRuntime() {
        return runtime;
    }

    public void setRuntime(WorkerRuntimeEntity runtime) {
        this.runtime = runtime;
    }
}
