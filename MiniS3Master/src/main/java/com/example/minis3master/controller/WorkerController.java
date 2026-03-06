package com.example.minis3master.controller;

import com.example.minis3master.db.worker.dto.WorkerFullDto;
import com.example.minis3master.db.worker.dto.FlatWorkerDto;
import com.example.minis3master.db.worker.entity.WorkerCapacityEntity;
import com.example.minis3master.db.worker.entity.WorkerEntity;
import com.example.minis3master.db.worker.entity.WorkerRuntimeEntity;
import com.example.minis3master.db.worker.service.WorkerPersistenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/workers")
@Tag(name = "Workers", description = "Worker management APIs")
public class WorkerController {

    private final WorkerPersistenceService workerService;

    private record SuccessResponse<T>(
            boolean success,
            String message,
            T data) {
    }

    private record ErrorResponse(
            boolean success,
            String message,
            String error,
            String path,
            Instant timestamp) {
    }

    private record RegisterWorkerRequest(
            String url,
            Long totalDiskSpace) {
    }

    public WorkerController(WorkerPersistenceService workerService) {
        this.workerService = workerService;
    }

    // GET /api/workers - Get all workers with combined data
    @GetMapping
    @Operation(summary = "Get all workers", description = "Fetches all workers with their capacity and runtime information combined")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched all workers")
    })
    public ResponseEntity<SuccessResponse<List<FlatWorkerDto>>> getAllWorkers() {
        List<FlatWorkerDto> workers = workerService.getAllWorkersFlat();

        SuccessResponse<List<FlatWorkerDto>> response = new SuccessResponse<>(
                true,
                "Workers fetched successfully",
                workers
        );
        return ResponseEntity.ok(response);
    }

    // GET /api/workers/{id} - Get single worker with combined data
    @GetMapping("/{id}")
    @Operation(summary = "Get worker by ID", description = "Fetches a single worker with capacity and runtime information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Worker found"),
            @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    public ResponseEntity<?> getWorkerById(
            @Parameter(description = "Worker ID", required = true)
            @PathVariable String id) {
        Optional<FlatWorkerDto> worker = workerService.getWorkerFlatById(id);

        if (worker.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Worker not found",
                    "No worker found with id: " + id,
                    "/api/workers/" + id,
                    Instant.now()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        SuccessResponse<FlatWorkerDto> response = new SuccessResponse<>(
                true,
                "Worker fetched successfully",
                worker.get()
        );
        return ResponseEntity.ok(response);
    }

    // POST /api/workers - Register a new worker
    @PostMapping
    @Operation(summary = "Register a new worker", description = "Creates a new worker with initial capacity and runtime status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Worker registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (missing URL)")
    })
    public ResponseEntity<?> registerWorker(@RequestBody RegisterWorkerRequest request) {
        if (request.url() == null || request.url().isBlank()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Validation failed",
                    "Worker URL is required",
                    "/api/workers",
                    Instant.now()
            );
            return ResponseEntity.badRequest().body(error);
        }

        String workerId = UUID.randomUUID().toString();
        Long now = Instant.now().toEpochMilli();

        // Create worker entity
        WorkerEntity worker = new WorkerEntity(workerId, request.url(), now, now);
        workerService.saveWorker(worker);

        // Create worker capacity entity
        Long totalDiskSpace = request.totalDiskSpace() != null ? request.totalDiskSpace() : 0L;
        WorkerCapacityEntity capacity = new WorkerCapacityEntity(workerId, totalDiskSpace, now);
        workerService.saveWorkerCapacity(capacity);

        // Create worker runtime entity with default values
        WorkerRuntimeEntity runtime = new WorkerRuntimeEntity(
                workerId,
                "ONLINE",
                now,
                totalDiskSpace,
                0,
                0,
                now
        );
        workerService.saveWorkerRuntime(runtime);

        // Fetch the complete worker data
        FlatWorkerDto flatWorker = workerService.getWorkerFlatById(workerId).orElseThrow();

        SuccessResponse<FlatWorkerDto> response = new SuccessResponse<>(
                true,
                "Worker registered successfully",
                flatWorker
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // DELETE /api/workers/{id} - Delete a worker
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a worker", description = "Removes a worker and all associated capacity and runtime records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Worker deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Worker not found")
    })
    public ResponseEntity<?> deleteWorker(
            @Parameter(description = "Worker ID", required = true)
            @PathVariable String id) {
        Optional<FlatWorkerDto> worker = workerService.getWorkerFlatById(id);

        if (worker.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Worker not found",
                    "No worker found with id: " + id,
                    "/api/workers/" + id,
                    Instant.now()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // Delete worker (cascade will handle capacity and runtime)
        workerService.deleteWorker(id);

        SuccessResponse<Map<String, String>> response = new SuccessResponse<>(
                true,
                "Worker deleted successfully",
                Map.of("id", id)
        );
        return ResponseEntity.ok(response);
    }
}

