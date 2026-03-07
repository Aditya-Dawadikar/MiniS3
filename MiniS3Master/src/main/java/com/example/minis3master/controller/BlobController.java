package com.example.minis3master.controller;

import com.example.minis3master.db.worker.entity.BlobEntity;
import com.example.minis3master.db.worker.entity.BlobReplicationEntity;
import com.example.minis3master.db.worker.service.BlobReplicationService;
import com.example.minis3master.db.worker.service.BlobSigningService;
import com.example.minis3master.db.worker.service.BlobService;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/blobs")
@Tag(name = "Blobs", description = "Blob metadata and signed URL APIs")
public class BlobController {

    private final BlobService blobService;
    private final BlobReplicationService blobReplicationService;
        private final BlobSigningService blobSigningService;

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

    private record BlobCreateRequest(
            String fileName,
            String fileSize) {
    }

    private record BlobUpdateRequest(
            String fileName,
            String fileSize,
            Long version) {
    }

    private record PagedBlobResponse(
            List<BlobEntity> items,
            int page,
            int size,
            long totalItems,
            int totalPages,
            boolean hasNext,
            boolean hasPrevious) {
    }

    private record InternalReplicationRequest(
            @JsonProperty("worker_id") String workerId,
            @JsonProperty("file_id") String fileId,
            @JsonProperty("replicated_at") Long replicatedAt) {
    }

    private record SignedPostUrlRequest(
            String fileName,
            String fileSize) {
    }

        private record SignedUrlResponse(
                        String fileId,
                        String method,
                        long expiresAt,
                        List<String> urls) {
        }

        public BlobController(BlobService blobService,
                                                  BlobReplicationService blobReplicationService,
                                                  BlobSigningService blobSigningService) {
        this.blobService = blobService;
        this.blobReplicationService = blobReplicationService;
                this.blobSigningService = blobSigningService;
    }

    @GetMapping
    @Operation(summary = "Get all blobs", description = "Fetches all blob metadata records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched all blobs")
    })
    public ResponseEntity<SuccessResponse<PagedBlobResponse>> getAllBlobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<BlobEntity> blobPage = blobService.getAllBlobs(page, size);
        PagedBlobResponse paged = new PagedBlobResponse(
                blobPage.getContent(),
                blobPage.getNumber(),
                blobPage.getSize(),
                blobPage.getTotalElements(),
                blobPage.getTotalPages(),
                blobPage.hasNext(),
                blobPage.hasPrevious());
        SuccessResponse<PagedBlobResponse> response = new SuccessResponse<>(
                true,
                "Blobs fetched successfully",
                paged);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "Get blob by ID", description = "Fetches one blob metadata record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blob found"),
            @ApiResponse(responseCode = "404", description = "Blob not found")
    })
    public ResponseEntity<?> getBlobById(
            @Parameter(description = "Blob file ID", required = true) @PathVariable String fileId) {
        Optional<BlobEntity> blob = blobService.getBlobById(fileId);

        if (blob.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Blob not found",
                    "No blob found with fileId: " + fileId,
                    "/api/blobs/" + fileId,
                    Instant.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        SuccessResponse<BlobEntity> response = new SuccessResponse<>(
                true,
                "Blob fetched successfully",
                blob.get());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a blob", description = "Creates a new blob metadata record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Blob created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<?> createBlob(@RequestBody BlobCreateRequest request) {
        if (request.fileName() == null || request.fileName().isBlank()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Validation failed",
                    "fileName is required",
                    "/api/blobs",
                    Instant.now());
            return ResponseEntity.badRequest().body(error);
        }

        String fileId = UUID.randomUUID().toString();
        long now = Instant.now().toEpochMilli();

        BlobEntity blob = new BlobEntity(
                fileId,
                request.fileName(),
                request.fileSize(),
                now,
                now,
                1L,
                0L,
                false,
                false,
                null,
                true);

        BlobEntity saved = blobService.saveBlob(blob);
        SuccessResponse<BlobEntity> response = new SuccessResponse<>(
                true,
                "Blob created successfully",
                saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{fileId}")
    @Operation(summary = "Update a blob", description = "Updates an existing blob metadata record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blob updated successfully"),
            @ApiResponse(responseCode = "404", description = "Blob not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<?> updateBlob(
            @Parameter(description = "Blob file ID", required = true) @PathVariable String fileId,
            @RequestBody BlobUpdateRequest request) {
        Optional<BlobEntity> existing = blobService.getBlobById(fileId);

        if (existing.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Blob not found",
                    "No blob found with fileId: " + fileId,
                    "/api/blobs/" + fileId,
                    Instant.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        BlobEntity currentBlob = existing.get();
        long now = Instant.now().toEpochMilli();
        String newFileId = UUID.randomUUID().toString();
        Long nextVersion = request.version() != null
                ? request.version()
                : (currentBlob.getVersion() == null ? 1L : currentBlob.getVersion() + 1L);

        String nextFileName = (request.fileName() != null && !request.fileName().isBlank())
                ? request.fileName()
                : currentBlob.getFileName();
        String nextFileSize = request.fileSize() != null ? request.fileSize() : currentBlob.getFileSize();

        currentBlob.setLatest(false);
        currentBlob.setUpdatedAt(now);
        blobService.saveBlob(currentBlob);

        BlobEntity updated = new BlobEntity(
                newFileId,
                nextFileName,
                nextFileSize,
                now,
                now,
                nextVersion,
                0L,
                false,
                false,
                currentBlob.getFileId(),
                true);
        BlobEntity savedVersion = blobService.saveBlob(updated);
        SuccessResponse<BlobEntity> response = new SuccessResponse<>(
                true,
                "Blob new version created successfully",
                savedVersion);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "Delete a blob", description = "Deletes a blob metadata record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blob deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Blob not found")
    })
    public ResponseEntity<?> deleteBlob(
            @Parameter(description = "Blob file ID", required = true) @PathVariable String fileId) {
        Optional<BlobEntity> existing = blobService.getBlobById(fileId);

        if (existing.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Blob not found",
                    "No blob found with fileId: " + fileId,
                    "/api/blobs/" + fileId,
                    Instant.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        blobService.deleteBlob(fileId);

        SuccessResponse<Map<String, String>> response = new SuccessResponse<>(
                true,
                "Blob soft deleted successfully",
                Map.of("fileId", fileId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fileId}/signed-url/get")
    @Operation(summary = "Generate signed GET URL", description = "Generates up to 3 signed download URLs for file replicas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signed URLs generated"),
            @ApiResponse(responseCode = "404", description = "Blob not found or no replica workers available")
    })
    public ResponseEntity<?> generateSignedGetUrl(
            @Parameter(description = "Blob file ID", required = true) @PathVariable String fileId) {
        Optional<BlobEntity> blob = blobService.getBlobById(fileId);
        if (blob.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Blob not found",
                    "No blob found with fileId: " + fileId,
                    "/api/blobs/" + fileId + "/signed-url/get",
                    Instant.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        BlobSigningService.SignedUrls signedUrls = blobSigningService.generateSignedGetUrls(fileId);
        if (signedUrls.urls().isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "No replicas available",
                    "No replica workers found for fileId: " + fileId,
                    "/api/blobs/" + fileId + "/signed-url/get",
                    Instant.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        SignedUrlResponse data = new SignedUrlResponse(fileId, "GET", signedUrls.expiresAt(), signedUrls.urls());
        SuccessResponse<SignedUrlResponse> response = new SuccessResponse<>(
                true,
                "Signed GET URLs generated successfully",
                data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signed-url/post")
    @Operation(summary = "Generate signed POST URL", description = "Creates a new blob metadata record and returns signed upload URLs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Blob created and signed URLs generated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "No workers available")
    })
    public ResponseEntity<?> generateSignedPostUrl(@RequestBody SignedPostUrlRequest request) {
        if (request.fileName() == null || request.fileName().isBlank()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Validation failed",
                    "fileName is required",
                    "/api/blobs/signed-url/post",
                    Instant.now());
            return ResponseEntity.badRequest().body(error);
        }

        String fileId = UUID.randomUUID().toString();
        long now = Instant.now().toEpochMilli();
        BlobSigningService.SignedUrls signedUrls = blobSigningService.generateSignedPostUrls(fileId);
        if (signedUrls.urls().isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "No workers available",
                    "No workers found to generate upload URLs",
                    "/api/blobs/signed-url/post",
                    Instant.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        BlobEntity blob = new BlobEntity(
                fileId,
                request.fileName(),
                request.fileSize(),
                now,
                now,
                1L,
                0L,
                false,
                false,
                null,
                true);
        blobService.saveBlob(blob);

        SignedUrlResponse data = new SignedUrlResponse(fileId, "POST", signedUrls.expiresAt(), signedUrls.urls());
        SuccessResponse<SignedUrlResponse> response = new SuccessResponse<>(
                true,
                "Signed POST URLs generated successfully",
                data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/internal/replication")
    @Operation(summary = "Internal replication callback", description = "Creates a replication record and increments blob replica count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Replication recorded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Blob not found")
    })
    public ResponseEntity<?> addReplicationRecord(@RequestBody InternalReplicationRequest request) {
        if (request.workerId() == null || request.workerId().isBlank() ||
                request.fileId() == null || request.fileId().isBlank()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Validation failed",
                    "worker_id and file_id are required",
                    "/api/blobs/internal/replication",
                    Instant.now());
            return ResponseEntity.badRequest().body(error);
        }

        Optional<BlobEntity> existingBlob = blobService.getBlobById(request.fileId());
        if (existingBlob.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    false,
                    "Blob not found",
                    "No blob found with fileId: " + request.fileId(),
                    "/api/blobs/internal/replication",
                    Instant.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        long replicatedAt = request.replicatedAt() != null ? request.replicatedAt() : Instant.now().toEpochMilli();
        BlobReplicationEntity replicationEntity = new BlobReplicationEntity(
                request.workerId(),
                request.fileId(),
                replicatedAt,
                false);
        BlobReplicationEntity savedReplication = blobReplicationService.saveBlobReplication(replicationEntity);

        BlobEntity blob = existingBlob.get();
        long finalReplicaCount = blobReplicationService.getActiveReplicaCountByFileId(request.fileId());
        blob.setReplicaCount(finalReplicaCount);
        blob.setUpdatedAt(Instant.now().toEpochMilli());
        blobService.saveBlob(blob);

        SuccessResponse<BlobReplicationEntity> response = new SuccessResponse<>(
                true,
                "Replication recorded successfully",
                savedReplication);
        return ResponseEntity.ok(response);
    }
}
